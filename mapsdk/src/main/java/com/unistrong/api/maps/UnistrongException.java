package com.unistrong.api.maps;

public class UnistrongException
  extends Exception
{
    /**
     * 输入输出异常。
     */
  public static final String ERROR_IO = "IO 操作异常 - IOException";
    /**
     * socket 连接异常。
     */
  public static final String ERROR_SOCKET = "socket 连接异常 - SocketException";
    /**
     * socket 连接超时。
     */
  public static final String ERROR_SOCKE_TIME_OUT = "socket 连接超时 - SocketTimeoutException";
    /**
     * 无效的参数。
     */
  public static final String ERROR_INVALID_PARAMETER = "无效的参数 - IllegalArgumentException";
    /**
     * 空指针异常。
     */
  public static final String ERROR_NULL_PARAMETER = "空指针异常 - NullPointException";
    /**
     * url异常。
     */
  public static final String ERROR_URL = "url异常 - MalformedURLException";
    /**
     * 未知主机。
     */
  public static final String ERROR_UNKNOW_HOST = "未知主机 - UnKnowHostException";
    /**
     * 服务器连接失败。
     */
  public static final String ERROR_UNKNOW_SERVICE = "服务器连接失败 - UnknownServiceException";
    /**
     * 协议解析错误。
     */
  public static final String ERROR_PROTOCOL = "协议解析错误 - ProtocolException";
    /**
     * http连接失败。。
     */
  public static final String ERROR_CONNECTION = "http连接失败 - ConnectionException";
  /**
   * 无sd卡读写权限。
   */
  public static final String ERROR_SDCARD_READORWRITE_PERMISSION = "无sd卡读写权限";
  /**
     * 未知的错误。
     */
  public static final String ERROR_UNKNOWN = "未知的错误";
  /**
     * key鉴权失败。
     */
  public static final String ERROR_FAILURE_AUTH = "key鉴权失败";
  /**
     * 空间不足。
     */
  public static final String ERROR_NOT_ENOUGH_SPACE = "空间不足";
  /**
     * 不可写入异常。
     */
  public static final String ERROR_NOT_AVAILABLE = "不可写入异常";
  /**
     * 非法坐标值。
     */
  public static final String ERROR_ILLEGAL_VALUE = "非法坐标值";

  /**
   * 离线鉴权失败。
   */
  public static final String ERROR_LOCAL_AUTH_FAILURE = "离线鉴权失败";

  /**
   * 移动设备上未安装地图或地图版本较旧。
   */
  public static final String LMAP_NOT_SUPPORT = "移动设备上未安装地图或地图版本较旧";
    /**
     * 非法导航参数。
     */
  public static final String ILLEGAL_LMAP_ARGUMENT = "非法导航参数";
    /**
     * 未知的错误。
     */
  private String errorMessage = "未知的错误";

    /**
     *构造定位异常对象。
     * @param errorMessage  - 异常信息。
     */
  public UnistrongException(String errorMessage)
  {
    super(errorMessage);
    this.errorMessage = errorMessage;
  }

    /**
     * 构造定位异常对象。
     */
  public UnistrongException() {}

    /**
     * 获取异常信息。
     * @return 异常信息。
     */
  public String getErrorMessage()
  {
    return this.errorMessage;
  }
}
