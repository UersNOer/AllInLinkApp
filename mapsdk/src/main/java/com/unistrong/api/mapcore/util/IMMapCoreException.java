package com.unistrong.api.mapcore.util;

public class IMMapCoreException//bl
  extends Exception
{
  private String errorMessage = "未知的错误";
  private int errorCode = -1;
  
  public IMMapCoreException(String errorMessage)
  {
    super(errorMessage);
    this.errorMessage = errorMessage;
    getErrorCode(errorMessage);
  }
  
  public String getErrorMessage()
  {
    return this.errorMessage;
  }
  
  private void getErrorCode(String errorMessage)
  {
    if ("IO 操作异常 - IOException".equals(errorMessage)) {
      this.errorCode = 21;
    } else if ("socket 连接异常 - SocketException".equals(errorMessage)) {
      this.errorCode = 22;
    } else if ("socket 连接超时 - SocketTimeoutException".equals(errorMessage)) {
      this.errorCode = 23;
    } else if ("无效的参数 - IllegalArgumentException".equals(errorMessage)) {
      this.errorCode = 24;
    } else if ("空指针异常 - NullPointException".equals(errorMessage)) {
      this.errorCode = 25;
    } else if ("url异常 - MalformedURLException".equals(errorMessage)) {
      this.errorCode = 26;
    } else if ("未知主机 - UnKnowHostException".equals(errorMessage)) {
      this.errorCode = 27;
    } else if ("服务器连接失败 - UnknownServiceException".equals(errorMessage)) {
      this.errorCode = 28;
    } else if ("协议解析错误 - ProtocolException".equals(errorMessage)) {
      this.errorCode = 29;
    } else if ("http连接失败 - ConnectionException".equals(errorMessage)) {
      this.errorCode = 30;
    } else if ("未知的错误".equals(errorMessage)) {
      this.errorCode = 31;
    } else if ("key鉴权失败".equals(errorMessage)) {
      this.errorCode = 32;
    } else if ("requeust is null".equals(errorMessage)) {
      this.errorCode = 1;
    } else if ("request url is empty".equals(errorMessage)) {
      this.errorCode = 2;
    } else if ("response is null".equals(errorMessage)) {
      this.errorCode = 3;
    } else if ("thread pool has exception".equals(errorMessage)) {
      this.errorCode = 4;
    } else if ("sdk name is invalid".equals(errorMessage)) {
      this.errorCode = 5;
    } else if ("sdk info is null".equals(errorMessage)) {
      this.errorCode = 6;
    } else if ("sdk packages is null".equals(errorMessage)) {
      this.errorCode = 7;
    } else if ("线程池为空".equals(errorMessage)) {
      this.errorCode = 8;
    } else if ("获取对象错误".equals(errorMessage)) {
      this.errorCode = 101;
    } else {
      this.errorCode = -1;
    }
  }
}
