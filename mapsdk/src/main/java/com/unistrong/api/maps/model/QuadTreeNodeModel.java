package com.unistrong.api.maps.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.unistrong.api.mapcore.IMapDelegate;
import com.unistrong.api.mapcore.MapDelegateImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表示一个聚合点,代表一个或多个poi
 * <p>{@link QuadTreeNodeModel#getSeqId()}用唯一id来去重,规则是点个数大于1时,id由其所在范围的起始点坐标
 * 和聚合点的size来生成，当前范围的点增加或减少时id会动态改变,保证了聚合时产生一个新的聚合点</p>
 */
public class QuadTreeNodeModel {
    // 网格范围的起始点坐标
    protected int x,y;
    //聚合点的经纬度
    protected LatLng lng;
    //聚合点包含的所有点集合
    protected List<QuadTreeNodeData> quadTreeNodeDataList = new ArrayList<QuadTreeNodeData>();
    //显示的mark
    protected Marker mMarker;
    //状态 0.正常 1.新增 2.修改 3.删除
    protected int status = 1;
    //地图代理索引
    protected IMapDelegate imapDelegate;
    //聚合样式图片缓存
    private LruCache<Integer, BitmapDescriptor> mLruCache;
    //图片
    private BitmapDescriptor bitmapDescriptor;
    public QuadTreeNodeModel(IMapDelegate imapDelegate, LruCache<Integer, BitmapDescriptor> mLruCache){
        this.imapDelegate = imapDelegate;
        this.mLruCache = mLruCache;
    }

    /**
     * 返回id（用于判断点聚合的重复）
     * @return
     */
    public String getSeqId(){
        if(quadTreeNodeDataList.size() == 1){
            return quadTreeNodeDataList.get(0).SEQUENCE;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(x);
        sb.append("_");
        sb.append(y);
        sb.append("-");
        sb.append(quadTreeNodeDataList.size());
        return sb.toString();
    }

    /**
     * 1和3修改
     * 将marker加入到地图中
     */
    public void addMarkerToMap(){
        if(lng == null){
            return;
        }
        //正常状态不管
        if(status == 0){
            return;
        }

        //删除状态移除
        if(status == 3 ){
            clearMarker();
            return;
        }

        try {
            //单个marker
            if(quadTreeNodeDataList.size() == 1){
                QuadTreeNodeData iquad = quadTreeNodeDataList.get(0);
                MarkerOptions options = iquad.getMarkerOptions();
                addMark(options,false);
            }else{
                MarkerOptions options = new MarkerOptions();
                addMark(options,true);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加marker
     * @param options
     * @param isQuad    是否为makr点
     * @throws RemoteException
     */
    public void addMark(MarkerOptions options,boolean isQuad) throws RemoteException {
        if(isQuad){
            options.position(lng);
        }
        StringBuilder sb = new StringBuilder();
        if(quadTreeNodeDataList.size() == 1){
            sb.append(quadTreeNodeDataList.get(0).SEQUENCE);
        }else {
            sb.append(lng.latitude);
            sb.append(lng.longitude);
            //自定义view
            getBitmapDesc(quadTreeNodeDataList.size());
            options.icon(bitmapDescriptor);
        }
        mMarker = imapDelegate.addMarker(options);
        mMarker.setObject(quadTreeNodeDataList);
        status = 0;
    }

    /**
     * 移除marker
     */
    public void clearMarker(){
        if(mMarker == null){
            return;
        }
        mMarker.remove();
        mMarker=null;
        status = 3;
    }

    private void getBitmapDesc(int number){
        bitmapDescriptor = mLruCache.get(number);
        if(bitmapDescriptor == null){
            TextView textView = new TextView(((MapDelegateImp)this.imapDelegate).getContext());
            String tile = String.valueOf(number);
            textView.setText(tile);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if(getDrawAble(number) != null){
                textView.setBackgroundDrawable(getDrawAble(number));
            }
            bitmapDescriptor = BitmapDescriptorFactory.fromView(textView);
            mLruCache.put(number, bitmapDescriptor);
        }
    }

    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
    Drawable getDrawAble(int clusterNum){
        int radius = dp2px(((MapDelegateImp)this.imapDelegate).getContext(), 40);
        if (clusterNum < 11) {

            Drawable bitmapDrawable = mBackDrawAbles.get(2);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.parseColor("#178DAB")));
                mBackDrawAbles.put(2, bitmapDrawable);
            }

            return bitmapDrawable;
        } else if (clusterNum < 151) {
            Drawable bitmapDrawable = mBackDrawAbles.get(3);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.parseColor("#54B780")));
                mBackDrawAbles.put(3, bitmapDrawable);
            }

            return bitmapDrawable;
        } else {
            Drawable bitmapDrawable = mBackDrawAbles.get(4);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.parseColor("#EC5A5B")));
                mBackDrawAbles.put(4, bitmapDrawable);
            }

            return bitmapDrawable;
        }
    };

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private Bitmap drawCircle(int radius, int color) {
        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
                Bitmap.Config.ARGB_8888);
        synchronized (bitmap){
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
            paint.setColor(color);
            canvas.drawArc(rectF, 0, 360, true, paint);
        }
        return bitmap;
    }


}
