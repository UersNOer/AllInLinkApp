package com.leador.mapcore.geojson;

import com.unistrong.api.maps.MapController;
import com.leador.mapcore.SingalThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GeoJsonTaskManager extends SingalThread {

    private static List<GeoJsonDownTile> downTileList=new ArrayList<>();
    private List<GeoJsonWorkerTask> threadPoolList=new ArrayList<>();
    private GeoJsonCallback callback;
    private static final int MAX_THREAD_COUNT=2;//线程池中线程数量
    private GeoJsonWorkerTask tempTask;
    private ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
    boolean threadFlag = true;
    private String user_key="";
    private MapController.GeoJsonServerListener serverListener;
    private CacheManager cacheManager;
    public GeoJsonTaskManager(GeoJsonCallback callback,String user_key,CacheManager cacheManager){
        this.callback=callback;
        this.user_key=user_key;
        this.cacheManager=cacheManager;
        start();
    }

    public MapController.GeoJsonServerListener getServerListener() {
        return serverListener;
    }

    public void setServerListener(MapController.GeoJsonServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public void updataConntionList(List<GeoJsonDownTile> list){
        synchronized (this.downTileList){
            if(list==null)return;
            downTileList.clear();
            cacheManager.setScreenTileList(list);
            for(GeoJsonDownTile tile:list)
                if(tile.isNeedDown())
            downTileList.add(tile);
        }
            doAwake();
    }
    private void checkListPool(){
        GeoJsonDownTile tile= null;
        GeoJsonWorkerTask task = null;
        ArrayList localArrayList = new ArrayList();
        ArrayList tileList = new ArrayList();
        int i = this.threadPoolList.size();
        for (int j = 0; j < i; j++)
        {
            task = (GeoJsonWorkerTask)this.threadPoolList.get(j);
            tile = task.getTile();
            if  (task.hasFinished())
            {
                localArrayList.add(task);
            }else{
                for(GeoJsonDownTile newTile:downTileList){
                    if(newTile.getId_tile()==tile.getId_tile()){
                        tileList.add(newTile);
                    }
                }
            }
        }
        if(tileList.size()>0)
        this.downTileList.removeAll(tileList);
        if(localArrayList.size()>0)
        this.threadPoolList.removeAll(localArrayList);
    }

        public void cancel(){
            downTileList.clear();
            for(GeoJsonWorkerTask task:this.threadPoolList){
                if(!task.hasFinished())task.doCancel();
            }
            this.threadPoolList.clear();
            try {
                doWait();
            } catch (InterruptedException localException) {
                localException.printStackTrace();
            }
        }

    private void doAsyncRequest()
    {
        while (this.threadFlag)
        {
            int i = 0;
            synchronized (this.downTileList)
            {
                GeoJsonDownTile tile = null;
                checkListPool();
                if(tempTask!=null&&tempTask.hasFinished()&&tempTask.isTimeOut&&tempTask.timeOutCount<3)
                {
                    tempTask.isFinished=false;
                    tempTask.mCanceled=false;
                    threadPoolList.add(tempTask);
                    this.threadPool.execute(tempTask);
                }
                while (this.downTileList.size() > 0)
                {
                    if (this.threadPoolList.size() > MAX_THREAD_COUNT)
                    {
                        i = 1;
                        break;
                    }
                    tile = (GeoJsonDownTile)this.downTileList.remove(0);
                    GeoJsonWorkerTask task = new GeoJsonWorkerTask(tile, this.callback,
                            this.user_key, this.serverListener,cacheManager);
                    this.threadPoolList.add(task);
                    if (!this.threadPool.isShutdown()) {
                        this.threadPool.execute(task);
                    }
                    tempTask=task;
                }
            }
            if (i != 0)
            {
                try
                {
                    sleep(30L);
                }
                catch (Exception localException)
                {
                }
            }
            else if (this.threadFlag)
            {
                try
                {
                    doWait();
                }
                catch (Throwable localThrowable)
                {
                }
            }
        }
    }
    public void run()
    {
        try
        {
            doAsyncRequest();
        }
        catch (Throwable localThrowable)
        {
            localThrowable.printStackTrace();
        }
    }

    public synchronized void destory(){
        threadFlag=false;
        if(isAlive()){
            interrupt();
            shutDown();}
//        CacheManager.release();
        if (cacheManager != null){
            cacheManager.clearCache();
        }
    }
    private void shutDown()
    {
        if (this.threadPool != null) {
            this.threadPool.shutdownNow();
        }
    }

}

