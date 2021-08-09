package com.unistrong.api.mapcore.util;

import android.content.Context;


import com.leador.mapcore.MapTilsCacheAndResManager;

import org.apache.http.util.ByteArrayBuffer;

/**
 *
 */
public class StylesIconsUpdate extends Thread {

    MapTilsCacheAndResManager.RetStyleIconsFile mRetFileRecoder;
    Context mContext;
    ByteArrayBuffer netWorkData = null;
    int mReceiveDataLen = 0;

    private volatile boolean canceled = false;
    private boolean successed = true;
    private int mode;
    private int updateServerVersion = 0;

    /**
     * @param context
     * @param ret
     */
    public StylesIconsUpdate(Context context, MapTilsCacheAndResManager.RetStyleIconsFile ret,int styleIconMode) {
        mRetFileRecoder = ret;
        mContext = context;
        this.mode=styleIconMode;
    }

    public void cancel() {
        canceled = true;
    }
    protected String getMapAddress()
    {
        return Util.getMapUrl(mContext)+"?";
    }
    @Override
    public void run() {
//        String name="style";
//        if(mode==1)name="icon";
//        String params = "t=VMMV4&type=30&name="+name + mRetFileRecoder.name.charAt(1) + "&cv="  + mRetFileRecoder.clientVersion + "&sv="  + mRetFileRecoder.serverVersion;
//        String tURL = getMapAddress() + params + "&ent=2";
//        android.util.Log.i("TAG",tURL);
////        android.util.Log.e("StylesLconsUpdate","url:"+tURL);
//        HttpParams httpParameters = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
//        HttpConnectionParams.setSoTimeout(httpParameters, 10000);
//        HttpConnectionParams.setSocketBufferSize(httpParameters, 1000);
//        InputStream inputStream = null;
//        HttpClient httpClient = new DefaultHttpClient(httpParameters);
//        HttpGet httpGet = new HttpGet(tURL);
//        HttpResponse httpresponse;
//        try {
//            httpresponse = httpClient.execute(httpGet);
//            if (HttpStatus.SC_OK == httpresponse.getStatusLine()
//                    .getStatusCode()) {
//
//                HttpEntity entity = httpresponse.getEntity();
//                inputStream = entity.getContent();
//
//                netWorkData = new ByteArrayBuffer(inputStream.available());
//                byte[] bufferByte = new byte[512];
//                int l = -1;
//                while ((l = inputStream.read(bufferByte)) > -1 && !canceled) {
//                    netWorkData.append(bufferByte, 0, l);
//                    mReceiveDataLen += l;
//                }
//
//                if (!canceled) {
//                    // 解析
//                    int index = 0;
//                    byte[] data = netWorkData.buffer();
//                    if (mReceiveDataLen > 11) {
//                        int flag = Convert.getInt(data, index);
//                        index += 4;
//                        if (flag == 0) {
//                            int dataLen = Convert.getInt(data, index);
//                            index += 4;
//                            if (dataLen > 0 && mReceiveDataLen - 4 == dataLen) {// 判断数据长度
//                                updateServerVersion = Convert.getInt(data, index);
//                                index += 4;
//                                if (updateServerVersion > mRetFileRecoder.serverVersion) {
//                                    successed = true;
//                                }
//                            }
//                        }
//                    }
//                }
////                successed = true;
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            if (successed) {
//                int fileLen = mReceiveDataLen - 12;
//                byte[] tmp = new byte[fileLen];
//                System.arraycopy(netWorkData.buffer(), 12, tmp, 0, fileLen);

//                MapTilsCacheAndResManager.getInstance(mContext).saveFile(
//                        mRetFileRecoder.name, mRetFileRecoder.clientVersion,
//                        updateServerVersion, tmp);
            }
//        }
    }

}
