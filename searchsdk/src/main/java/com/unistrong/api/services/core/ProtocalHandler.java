package com.unistrong.api.services.core;

import android.content.Context;

import com.unistrong.api.services.poisearch.XGPoiJsonParser;
import com.unistrong.api.services.poisearch.XGPoiResult;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * ProtocalHandler
 * <p>
 * <p>
 * Base class with code to manage the network connecting
 *
 * @author
 * @version 1.0 2010.01.22<br>
 */
public abstract class ProtocalHandler<T, V> {

    public ProtocalHandler(Context context, T tsk, Proxy prx, String device) {
        this.context = context;
        proxy = prx;
        task = tsk;
        maxTry = HttpTool.MaxTry;
        timeoutSeconds = HttpTool.TimeoutSeconds;
        waitSeconds = HttpTool.WaitSeconds;
        mAgent = device;
        mKey = CoreUtil.getApiKey(context);
//		this.scode = CoreUtil.getScode(context);
    }

    public ProtocalHandler(Context context, Proxy prx, String device) {
        this.context = context;
        proxy = prx;
        maxTry = HttpTool.MaxTry;
        timeoutSeconds = HttpTool.TimeoutSeconds;
        waitSeconds = HttpTool.WaitSeconds;
        mAgent = device;
        mKey = CoreUtil.getApiKey(context);
//		this.scode = CoreUtil.getScode(context);
    }

    /*****************************************************************************/
    // Class Methods

    /*****************************************************************************/
    public void setTask(T tsk) {
        task = tsk;
    }

    /**
     * 是否为get请求
     *
     * @return true get,false post
     */
    abstract protected boolean getRequestType();

    abstract protected int getServiceCode();

    abstract protected String[] getRequestLines() throws UnistrongException;

    abstract protected V loadData(InputStream inputStream, boolean isGZip)
            throws UnistrongException;

    abstract protected String getUrl();

    protected Map<String, String> getHeadMaps() {
        SDKInfo sdkInfo = null;
        try {
            sdkInfo = new SDKInfo.createSDKInfo(ConfigableConstDecode.product,
                    CoreUtil.getVersion(),
                    ConfigableConstDecode.userAgent, "")
                    .setPackageName(new String[]{"com.leador.api.services"}).a();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        paramsMap.put("User-Agent", sdkInfo.useragen);
        paramsMap.put("X-INFO", ClientInfo.initXInfo(context, false));
        paramsMap.put("logversion", "1.2");
        paramsMap.put("platinfo", String.format("platform=Android&sdkversion=%s&product=%s",
                new Object[]{sdkInfo.version, sdkInfo.product}));
        return paramsMap;
    }

    ;

    protected byte[] makePostRequestBytes() throws UnistrongException {
        //post请求
//		String[] params=getRequestLines();
        StringBuilder sb = new StringBuilder(makeJSONRequest());
        if (!sb.toString().equals("")) {
            sb.append("&");
        }
        getKeyAndScode(sb);
        return sb.toString().getBytes();
    }

    private String makeJSONRequest() throws UnistrongException {
        String[] lines = getRequestLines();
        if (lines == null) {
            return "";
        }
        StringBuilder request = new StringBuilder();
        if (lines != null) {
            for (String line : lines) {
                request.append(line);
            }
        }
        return request.toString();
    }

    public static XGPoiResult GetPoiData(String poiName) throws UnistrongException {


//        final Map<String, String> map = new HashMap<>();
//        map.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        XGPoiResult result = null;
        InputStream input = null;
        //创建HttpURLConnection变量
        HttpURLConnection httpURLConnection = null;
        //创建PrintWriter变量，用于向HttpURLConnection写入请求参数
        PrintWriter printWriter = null;
        //创建BufferedReader，来接收相应数据流
        BufferedReader bufferedReader = null;
            //定义接口地址
        String url_path = "http://117.159.24.4:107/gisserver/service/poi_wfst/wfs-t?VERSION=1.0.0&REQUEST=GetFeature&SERVICE=WFS&TYPENAME=poi_wfst%3Apoi&RESULTTYPE=results&nsukey=kNpOgpZFY3HMoxmyaPA7YSnmE22LYUAWbkxyxJ3jJB8L2f5c%2B6LUhCnBdL%2BSV0Z%2B6ZtN7WlUpbhGoUzQJTGoAalnD8o4onnsJ1P9JXPZOwq8cpg%2BFo1194VWvyC7eW063Ji9c1JjdnUrh9oRQKO9XfSC64xpHyusoO846bGKNEl1RJo4kjrRKl4p6Bsuv5LzrHOBWorby3YJavhP7fDUTQ%3D%3D&cql_filter=poiname like '%25" + poiName + "%25'&outputFormat=application/json";
        //模拟参数
        try {
            URL url = null;
            if (url_path.equals("")) {
            }
            url = new URL(url_path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);    //可以创建输出流，将请求参数写入
            httpURLConnection.setRequestMethod("GET"); //请求方式为POST
            Iterator<String> keyIterator;
            int byteLength;
            byte[] buffer = new byte[1024];
            StringBuilder builder = new StringBuilder();
            input = httpURLConnection.getInputStream();
            while ((byteLength = input.read(buffer)) != -1)
                builder.append(new String(buffer, 0, byteLength, "UTF-8"));
            result = new XGPoiJsonParser().loadData(builder.toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                /**
                 * 关闭连接/流
                 */
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public V GetData() throws UnistrongException {
        V result = null;
        if (null != task) {
            result = GetDataMayThrow();
        }
        return result;
    }

    private V GetDataMayThrow() throws UnistrongException {
        HttpURLConnection conn = null;
        InputStream input = null;
        OutputStream output = null;
        V result = null;
        int trytime = 0;
        while (trytime < maxTry) {
            try {
                // 表示post请求
                if (getRequestType() == false) {
                    recommandURL = getUrl();
                    byte[] entityBytes = makePostRequestBytes();
                    conn = HttpTool.makePostRequest(recommandURL, entityBytes,
                            proxy, getHeadMaps());
                } else { // get 请求
                    StringBuilder sb = new StringBuilder(makeJSONRequest());
                    if (!sb.toString().equals("")) {
                        sb.append("&");
                    }
                    getKeyAndScode(sb);
                    recommandURL = getUrl() + sb.toString();
                    conn = HttpTool.makeGetRequest(recommandURL, proxy, getHeadMaps());
                }
//                Log.e("recommandURL = ", recommandURL);
                boolean isGZip = false;
                String encoding = conn.getContentEncoding();
                if (encoding != null && encoding.contains("gzip")) {//首先判断服务器返回的数据是否支持gzip压缩，
                    //如果支持则应该使用GZIPInputStream解压，否则会出现乱码无效数据
                    isGZip = true;
                }
                input = sendRequest(conn);
                result = parseResponse(input, isGZip);
                trytime = maxTry;
            } catch (UnistrongException ex) {
                trytime++;
                if (trytime < maxTry) {
                    try {
                        Thread.sleep(waitSeconds * 1000);
                    } catch (InterruptedException e) {
                        throw new UnistrongException(e.getMessage());
                    }
                } else {
                    result = onExceptionOccur();
                    throw new UnistrongException(ex.getErrorMessage());
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        throw new UnistrongException(UnistrongException.ERROR_IO);
                    }
                    input = null;
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        throw new UnistrongException(UnistrongException.ERROR_IO);
                    }
                    output = null;
                }
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
            }
        }
        return result;
    }


    private void getKeyAndScode(StringBuilder sb) {
        if (sb != null) {
            String ts = ClientInfo.getTS();
            this.scode = CoreUtil.getScode(this.context, ts);
            sb.append("ak=").append(this.mKey);
            sb.append("&scode=").append(this.scode);
            sb.append("&ts=").append(ts);
        }
    }

    protected InputStream sendRequest(HttpURLConnection conn)
            throws UnistrongException {
        InputStream inStream = null;
        try {
            inStream = conn.getInputStream();
        } catch (ProtocolException e) {
            throw new UnistrongException(UnistrongException.ERROR_PROTOCOL);
        } catch (UnknownHostException e) {
            throw new UnistrongException(UnistrongException.ERROR_UNKNOW_HOST);
        } catch (UnknownServiceException e) {
            throw new UnistrongException(UnistrongException.ERROR_UNKNOW_SERVICE);
        } catch (IOException e) {
            throw new UnistrongException(UnistrongException.ERROR_IO);
        }
        return inStream;
    }

    private V parseResponse(InputStream inputStream, boolean isGZip) throws UnistrongException {
        return loadData(getStreamData(inputStream), isGZip);
        // return loadData(inputStream);
    }

    protected void protocalAssert(boolean carteiea) throws IOException {
        if (!carteiea) {
            throw new IOException();
        }
    }

    protected V onExceptionOccur() {
        return null;
    }

    public int getError() {
        return error;
    }

    protected String makeTag(String tagName, boolean head) {
        String fmt = head ? "<%s>" : "</%s>";
        return String.format(fmt, tagName);
    }

    protected String makeContentLine(String tag, String content) {
        if (content == null) {
            content = "";
        }
        String line = makeTag(tag, true);
        line += content;
        line += makeTag(tag, false);
        return line;
    }

    private static InputStream getStreamData(InputStream is)
            throws UnistrongException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream arrayInputStream = null;
        byte[] bufferByte = new byte[2048];
        int readed = -1;
        // int downloadSize = 0;
        // byte[] btemp = null;
        try {
            while ((readed = is.read(bufferByte)) > -1) {
                // downloadSize += readed;
                out.write(bufferByte, 0, readed);
                out.flush();
            }
            arrayInputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new UnistrongException(UnistrongException.ERROR_IO);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
        }
        return arrayInputStream;
    }

    protected byte[] getBytes(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int iReadByt = -1;
        try {
            while ((iReadByt = inputStream.read()) != -1) {
                bytestream.write(iReadByt);
            }
        } catch (IOException ex) {
        }
        byte imgdata[] = bytestream.toByteArray();
        try {
            bytestream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytestream = null;
        return imgdata;
    }

    protected int getByte(InputStream inputStream) throws IOException {
        return inputStream.read();
    }

    protected int getInt(InputStream inputStream) throws IOException {
        byte[] b = new byte[4];
        inputStream.read(b, 0, 4);

        int result = ((b[3] & 0xff) << 24) + ((b[2] & 0xff) << 16)
                + ((b[1] & 0xff) << 8) + (b[0] & 0xff);
        return result;
    }

    protected int getShort(InputStream inputStream) throws IOException {
        byte[] b = new byte[2];
        inputStream.read(b, 0, 2);

        int result = ((b[1] & 0xff) << 8) + (b[0] & 0xff);
        return result;
    }

    protected String getString(InputStream inputStream) throws IOException {
        return readString(inputStream, getShort(inputStream));
    }

    protected String getIntString(InputStream inputStream) throws IOException {
        return readString(inputStream, getInt(inputStream));
    }

    protected String get1BString(InputStream inputStream) throws IOException {
        return readString(inputStream, getByte(inputStream));
    }

    private String readString(InputStream inputStream, int len)
            throws IOException {
        byte[] b = new byte[len];
        try {
            int cnt = 0;
            while (cnt < len) {
                cnt += inputStream.read(b, cnt, len - cnt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(b, "utf-8");
    }

    protected String readString(InputStream inputStream) throws UnistrongException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = (inputStream.read(buffer))) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
        } catch (IOException e) {
            throw new UnistrongException(UnistrongException.ERROR_IO);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
        }
        try {
            return new String(outStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String();
    }

    protected String zipInputStream(InputStream is) throws UnistrongException {
        StringBuffer buffer = null;
        GZIPInputStream gzip = null;
        try {
            gzip = new GZIPInputStream(is);
            BufferedReader in = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
            buffer = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null)
                buffer.append(line + "\n");
            is.close();
        } catch (IOException e) {
            throw new UnistrongException(UnistrongException.ERROR_IO);
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
        }
        if (buffer != null) {
            return buffer.toString();
        }
        return new String();
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    /******************************************************************************/
    // Class Members
    /******************************************************************************/
    protected Proxy proxy;
    protected T task;
    protected int maxTry = 1;
    protected int timeoutSeconds = 20;
    protected int waitSeconds = 0;
    protected int error = 0;
    protected String mKey = "";
    protected String scode = "";
    protected String ts = "";
    // private final String OGTail = "</og>";
    protected Context context;
    protected String mAgent;
    protected String recommandURL = "";// ServerURL;
    public static final int IOError = -999;
    public static final int SocketError = -1000;
    public static final int NoError = 0;


    public interface serachPoi {
        void sercSuccess(XGPoiResult data);
    }
}
