package com.unistrong.api.mapcore.util;

import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUrlUtil//dm
{
  private static NetCompleteListener netCompleteListener;
  private int connTimeout;
  private int readTimeout;
  private boolean isSSL;
  private SSLContext e;
  private Proxy proxy;
  private volatile boolean g = false;
  private long h = -1L;
  private long i = 0L;
  
  public static void setNetCompleteListener(NetCompleteListener listener)
  {
    netCompleteListener = listener;
  }
  
  HttpUrlUtil(int connTimeout, int readTimeout, Proxy paramProxy, boolean isSSL)
  {
    this.connTimeout = connTimeout;
    this.readTimeout = readTimeout;
    this.proxy = paramProxy;
    this.isSSL = isSSL;
    if (isSSL) {
      try
      {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
          @Override
          public void checkClientTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
          }
          @Override
          public void checkServerTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
          }
          @Override
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }
        };
        sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());
        this.e = sslContext;
      }
      catch (NoSuchAlgorithmException exception)
      {
        BasicLogHandler.a(exception, "HttpUrlUtil", "HttpUrlUtil");
      }
      catch (Throwable localThrowable)
      {
        BasicLogHandler.a(localThrowable, "HttpUtil", "HttpUtil");
      }
    }
  }
  
  HttpUrlUtil(int connTimeout, int readTimeout, Proxy paramProxy)
  {
    this(connTimeout, readTimeout, paramProxy, false);
  }
  
  void a()
  {
    this.g = true;
  }
  
  void a(long paramLong)
  {
    this.i = paramLong;
  }
  
  void b(long paramLong)
  {
    this.h = paramLong;
  }
  
  
  
  ResponseEntity makeGetRequest(String paramString, Map<String, String> paramMap1, Map<String, String> paramMap2)
    throws IMMapCoreException
  {
    try
    {
      String str = getParaString(paramMap2);
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append(paramString);
      if (str != null) {
        localStringBuffer.append("?").append(str);
      }
      HttpURLConnection localHttpURLConnection = a(localStringBuffer.toString(), paramMap1, false);
      
      localHttpURLConnection.connect();
      return parseResult(localHttpURLConnection);
    }
    catch (ConnectException localConnectException)
    {
      throw new IMMapCoreException("http连接失败 - ConnectionException");
    }
    catch (MalformedURLException localMalformedURLException)
    {
      throw new IMMapCoreException("url异常 - MalformedURLException");
    }
    catch (UnknownHostException localUnknownHostException)
    {
      throw new IMMapCoreException("未知主机 - UnKnowHostException");
    }
    catch (SocketException localSocketException)
    {
      throw new IMMapCoreException("socket 连接异常 - SocketException");
    }
    catch (SocketTimeoutException localSocketTimeoutException)
    {
      throw new IMMapCoreException("socket 连接超时 - SocketTimeoutException");
    }
    catch (IOException localIOException)
    {
      throw new IMMapCoreException("IO 操作异常 - IOException");
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      throw new IMMapCoreException("未知的错误");
    }
  }
  
  ResponseEntity makePostReqeust(String url, Map<String, String> headsMap, byte[] paramArrayOfByte)
    throws IMMapCoreException
  {
    try
    {
      HttpURLConnection localHttpURLConnection = a(url, headsMap, true);
      if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0))
      {
        DataOutputStream localDataOutputStream = new DataOutputStream(localHttpURLConnection.getOutputStream());
        
        localDataOutputStream.write(paramArrayOfByte);
        localDataOutputStream.close();
      }
      localHttpURLConnection.connect();
      return parseResult(localHttpURLConnection);
    }
    catch (ConnectException localConnectException)
    {
      localConnectException.printStackTrace();
      throw new IMMapCoreException("http连接失败 - ConnectionException");
    }
    catch (MalformedURLException localMalformedURLException)
    {
      localMalformedURLException.printStackTrace();
      throw new IMMapCoreException("url异常 - MalformedURLException");
    }
    catch (UnknownHostException localUnknownHostException)
    {
      localUnknownHostException.printStackTrace();
      throw new IMMapCoreException("未知主机 - UnKnowHostException");
    }
    catch (SocketException localSocketException)
    {
      localSocketException.printStackTrace();
      throw new IMMapCoreException("socket 连接异常 - SocketException");
    }
    catch (SocketTimeoutException localSocketTimeoutException)
    {
      localSocketTimeoutException.printStackTrace();
      throw new IMMapCoreException("socket 连接超时 - SocketTimeoutException");
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      throw new IMMapCoreException("IO 操作异常 - IOException");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "HttpUrlUtil", "makePostReqeust");
      
      throw new IMMapCoreException("未知的错误");
    }
  }
  
  HttpURLConnection a(String url, Map<String, String> paramMap, boolean isPost)
    throws IOException
  {
    DeviceInfo.a();
    
    URL localURL = new URL(url);
    Object localObject2;
    if (this.proxy != null) {
      localObject2 = localURL.openConnection(this.proxy);
    } else {
      localObject2 = (HttpURLConnection)localURL.openConnection();
    }
    Object localObject1;
    if (this.isSSL)
    {
      localObject1 = (HttpsURLConnection)localObject2;
      ((HttpsURLConnection)localObject1).setSSLSocketFactory(this.e.getSocketFactory());
      
//      ((HttpsURLConnection)localObject1).setHostnameVerifier(this.j);
    }
    else
    {
      localObject1 = (HttpURLConnection)localObject2;
    }
    if ((Build.VERSION.SDK != null) && (Build.VERSION.SDK_INT > 13)) {
      ((HttpURLConnection)localObject1).setRequestProperty("Connection", "close");
    }
    addHeaders(paramMap, (HttpURLConnection) localObject1);
    if (isPost)
    {
      ((HttpURLConnection)localObject1).setRequestMethod("POST");
      ((HttpURLConnection)localObject1).setUseCaches(false);
      ((HttpURLConnection)localObject1).setDoInput(true);
      ((HttpURLConnection)localObject1).setDoOutput(true);
    }
    else
    {
      ((HttpURLConnection)localObject1).setRequestMethod("GET");
      ((HttpURLConnection)localObject1).setDoInput(true);
    }
    return (HttpURLConnection)localObject1;
  }
  
  private ResponseEntity parseResult(HttpURLConnection paramHttpURLConnection)
    throws IMMapCoreException, IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = null;
    InputStream localInputStream = null;
    Object localObject1 = null;
    PushbackInputStream localPushbackInputStream = null;
    try
    {

      Map<String, List<String>> localMap = paramHttpURLConnection.getHeaderFields();
      
      int k = paramHttpURLConnection.getResponseCode();
      if (k != 200) {
        throw new IMMapCoreException("网络异常原因：" + paramHttpURLConnection.getResponseMessage() + " 网络异常状态码：" + k);
      }
      localByteArrayOutputStream = new ByteArrayOutputStream();
      localInputStream = paramHttpURLConnection.getInputStream();
      localPushbackInputStream = new PushbackInputStream(localInputStream, 2);
      
      byte[] arrayOfByte1 = new byte[2];
      localPushbackInputStream.read(arrayOfByte1);
      localPushbackInputStream.unread(arrayOfByte1);
      if ((arrayOfByte1[0] == 31) && (arrayOfByte1[1] == -117)) {
        localObject1 = new GZIPInputStream(localPushbackInputStream);
      } else {
        localObject1 = localPushbackInputStream;
      }
      int m = 0;
      
      byte[] arrayOfByte2 = new byte[1024];
      while ((m = ((InputStream)localObject1).read(arrayOfByte2)) != -1) {
        localByteArrayOutputStream.write(arrayOfByte2, 0, m);
      }
      if (netCompleteListener != null) {
        netCompleteListener.complete();
      }
      ResponseEntity localdr1 = new ResponseEntity();
      localdr1.resBytes = localByteArrayOutputStream.toByteArray();
      localdr1.b = localMap;
      return localdr1;
    }
    catch (IOException localIOException1)
    {
      throw localIOException1;
    }
    finally
    {
      if (localByteArrayOutputStream != null) {
        try
        {
          localByteArrayOutputStream.close();
        }
        catch (IOException localIOException2)
        {
          BasicLogHandler.a(localIOException2, "HttpUrlUtil", "parseResult");
          
          localIOException2.printStackTrace();
        }
      }
      if (localInputStream != null) {
        try
        {
          localInputStream.close();
        }
        catch (Exception localException1)
        {
          BasicLogHandler.a(localException1, "HttpUrlUtil", "parseResult");
          
          localException1.printStackTrace();
        }
      }
      if (localPushbackInputStream != null) {
        try
        {
          localPushbackInputStream.close();
        }
        catch (Exception localException2)
        {
          BasicLogHandler.a(localException2, "HttpUrlUtil", "parseResult");
          
          localException2.printStackTrace();
        }
      }
      if (localObject1 != null) {
        try
        {
          ((InputStream)localObject1).close();
        }
        catch (Exception localException3)
        {
          BasicLogHandler.a(localException3, "HttpUrlUtil", "parseResult");
          
          localException3.printStackTrace();
        }
      }
      if (paramHttpURLConnection != null) {
        try
        {
          paramHttpURLConnection.disconnect();
        }
        catch (Throwable localThrowable)
        {
          BasicLogHandler.a(localThrowable, "HttpUrlUtil", "parseResult");
          
          localThrowable.printStackTrace();
        }
      }
    }
  }
  
  private void addHeaders(Map<String, String> headsMap, HttpURLConnection connection)
  {
    Iterator<String> keyIterator;
    if (headsMap != null) {
      for (keyIterator = headsMap.keySet().iterator(); keyIterator.hasNext();)
      {
        String key = (String)keyIterator.next();
        connection.addRequestProperty(key, (String) headsMap.get(key));
      }
    }
    try
    {
      UUID localObject2 = UUID.randomUUID();
      String str1 = ((UUID)localObject2).toString();
      String str2 = str1.replaceAll("-", "").toLowerCase();
      connection.addRequestProperty("csid", str2);
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "HttpUrlUtil", "addHeaders");
    }
    connection.setConnectTimeout(this.connTimeout);
    connection.setReadTimeout(this.readTimeout);
  }
  
  private HostnameVerifier j = new ds(this);
  
  static String getParaString(Map<String, String> paramMap)
  {
    if (paramMap != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      //for (Map.Entry localEntry : paramMap.entrySet())r
      for (Map.Entry<String,String> localEntry : paramMap.entrySet())  
      {
        String str1 = (String)localEntry.getKey();
        String str2 = (String)localEntry.getValue();
        if (str2 == null) {
          str2 = "";
        }
        if (localStringBuilder.length() > 0) {
          localStringBuilder.append("&");
        }
        localStringBuilder.append(URLEncoder.encode(str1));
        localStringBuilder.append("=");
        localStringBuilder.append(URLEncoder.encode(str2));
      }
      return localStringBuilder.toString();
    }
    return null;
  }

  class ds implements HostnameVerifier
  {
    ds(HttpUrlUtil paramdm) {}

    public boolean verify(String paramString, SSLSession paramSSLSession)
    {
      HostnameVerifier localHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

      return localHostnameVerifier.verify("*.leador.com", paramSSLSession);
    }
  }
}
