package com.unistrong.api.services.core;

/**
 * 服务异常信息类。
 * 
 */
public class UnistrongException extends Exception {

    public static final String ERROR_IO = "IO 操作异常 - IOException";
    public static final String ERROR_SOCKET = "socket 连接异常 - SocketException";
    public static final String ERROR_SOCKE_TIME_OUT = "socket 连接超时 - SocketTimeoutException";
    public static final String ERROR_INVALID_PARAMETER = "无效的参数 - IllegalArgumentException";
    public static final String ERROR_NULL_PARAMETER = "空指针异常 - NullPointException";
    public static final String ERROR_URL = "url异常 - MalformedURLException";
    public static final String ERROR_UNKNOW_HOST = "未知主机 - UnKnowHostException";
    public static final String ERROR_UNKNOW_SERVICE = "服务器连接失败 - UnknownServiceException";
    public static final String ERROR_PROTOCOL = "协议解析错误 - ProtocolException";
    public static final String ERROR_CONNECTION = "http连接失败 - ConnectionException";
    public static final String ERROR_UNKNOWN = "未知的错误";
    public static final String ERROR_FAILURE_AUTH = "用户Key校验未通过";
    public static final String ERROR_Available_key = "用户KEY不可用";
    public static final String ERROR_INVALID_AUTH = "用户KEY已过期";
    public static final String ERROR_NOSUCHALGORITHM = "NoSuchAlgorithmException";
    public static final String ERROR_KEYMANAGEMENT = "KeyManagementException";

    public static final String ERROR_SERVICE = "查询结果为空";
    public static final String ERROR_ENGINE = "引擎异常";
    public static final String ERROR_SERVER = "服务不存在或服务正在维护中";
    public static final String ERROR_QUOTA = "请求次数超过配额";
    public static final String ERROR_REQUEST = "参数校验错误,请求参数非法";//参数校验错误,请求参数非法[tactics值输入错误]
    public static final String ERROR_REQUEST1 = "参数校验错误,缺少必填参数";
    public static final String ERROR_USERID = "USERID非法";
    public static final String MAP_SIGNATURE_ERROR = "签名错误";
    public static final String ERROR_ROUTE_FAILURE = "路线计算失败";
    public static final String ERROR_ROUTE_BUS_FAILURE = "起点终点步行距离过短，即步行可达";
    public static final String ERROR_ROUTE_BUS_FAILURE1 = "起点步行距离过长";
    public static final String ERROR_ROUTE_BUS_FAILURE2 = "终点步行距离过长";
    public static final String ERROR_ROUTE_BUS_FAILURE3 = "该城市没有公交数据";
    public static final String ERROR_OVER_DIRECTION_RANGE = "起点终点步行距离过长";
    public static final String ERROR_OUT_OF_SERVICE = "规划点（起点）不在中国陆地范围内";
    public static final String ERROR_OUT_OF_SERVICE_END = "规划点(终点）不在中国陆地范围内";
    public static final String ERROR_OUT_OF_SERVICE_NOROAD = "起始点没路";
    public static final String ERROR_OUT_OF_SERVICE_NOROAD_END = "终点没路";
    public static final String ERROR_OUT_OF_SERVICE_NOROAD_AROUND= "起点附近没有找到道路";
    public static final String ERROR_OUT_OF_SERVICE_NOROAD__AROUND_END = "终点附近没有找到道路";
    public static final String ERROR_OUT_OF_SERVICE_NOROAD__AROUND_POLYLINE = "终点附近没有找到道路";


    public static final String ERROR_SCODE = "用户MD5安全码未通过";
    public static final String ERROR_PB_STATE3 = "非法坐标格式-010001";
    public static final String ERROR_PB_STATE4 = "字符集编码错误-010002";
    public static final String ERROR_PB_STATE10 = "IP验证失败-020004";
    public static final String ERROR_PB_STATE11 = "城市验证失败-020005";
    public static final String ERROR_PB_STATE12 = "基础模型验证失败-020006";
    public static final String ERROR_PB_STATE13 = "网卡地址不匹配-020007";
    public static final String ERROR_PB_STATE14 = "license配置错误-020008";
    public static final String ERROR_PB_STATE15 = "城市号不匹配-020009";
    public static final String ERROR_PB_STATE16 = "头文件不匹配-020010";
    public static final String ERROR_PB_STATE17 = "请求数超出最大范围-020011";
    public static final String ERROR_PB_STATE18 = "缓存服务器异常-030001";
    public static final String ERROR_PB_STATE19 = "查询服务连接异常-040001";
    public static final String ERROR_PB_STATE20 = "查询服务返回格式解析异常-040002";
    public static final String ERROR_PB_STATE21 = "当前格式不支持-050001";





    public static final String ERROR_NAME_NOT_FOUND = "地图key未配置 － NameNotFoundException";
    /**
     * 离线鉴权失败。
     */
    public static final String ERROR_LOCAL_AUTH_FAILURE = "离线鉴权失败";

    /**
     * 鉴权文件路径错误。
     */
    public static final String ERROR_LOCAL_PATH = "数据路径或者鉴权文件路径异常";
    public static final int ERROR_CODE_UPLOAD_SHORT_INTERVAL = 11;
    public static final int ERROR_CODE_UPLOAD_EMPTY_OBJECT = 12;
    public static final int ERROR_CODE_UPLOAD_WRONG_ID = 13;
    public static final int ERROR_CODE_UPLOAD_WRONG_POINT = 14;
    public static final int ERROR_CODE_UPLOADAUTO_STARTED = 15;
    public static final int ERROR_CODE_BINDER_KEY = 16;
    public static final int ERROR_CODE_IO = 21;
    public static final int ERROR_CODE_SOCKET = 22;
    public static final int ERROR_CODE_SOCKE_TIME_OUT = 23;
    public static final int ERROR_CODE_INVALID_PARAMETER = 24;
    public static final int ERROR_CODE_NULL_PARAMETER = 25;
    public static final int ERROR_CODE_URL = 26;
    public static final int ERROR_CODE_UNKNOW_HOST = 27;
    public static final int ERROR_CODE_UNKNOW_SERVICE = 28;
    public static final int ERROR_CODE_PROTOCOL = 104;
    public static final int ERROR_CODE_CONNECTION = 30;
    public static final int ERROR_CODE_UNKNOWN = 31;
    public static final int ERROR_CODE_FAILURE_AUTH = 102;
    public static final int ERROR_CODE_Available_key = 112;
    public static final int ERROR_CODE_INVALID_AUTH= 113;
    public static final int ERROR_CODE_SERVICE = 105;
    public static final int ERROR_CODE_SERVER = 404;
    public static final int ERROR_CODE_ENGINE= 10000;
    public static final int ERROR_CODE_QUOTA = 103;
    public static final int ERROR_CODE_REQUEST = 111;
    public static final int ERROR_CODE_REQUEST1= 101;
    public static final int ERROR_CODE_SHARE_SEARCH_FAILURE = 37;
    public static final int MAP_LICENSE_IS_EXPIRED_CODE = 1901;
    public static final int ERROR_CODE_USERKEY_PLAT_NOMATCH = 39;
    public static final int MAP_SIGNATURE_ERROR_CODE = 1001;
    public static final int ERROR_CODE_ROUTE_FAILURE = 43;
    public static final int ERROR_CODE_OVER_DIRECTION_RANGE = 10181;
    public static final int ERROR_CODE_OUT_OF_SERVICE = 10006;
    public static final int ERROR_CODE_OUT_OF_SERVICE_END= 10007;
    public static final int ERROR_CODE_OUT_OF_SERVICE_NOROAD= 10080;
    public static final int ERROR_CODE_OUT_OF_SERVICE_NOROAD_END= 10081;
    public static final int ERROR_CODE_OUT_OF_SERVICE_NOROAD_AROUND= 10008;
    public static final int ERROR_CODE_OUT_OF_SERVICE_NOROAD_AROUND_END= 10009;
    public static final int ERROR_CODE_OUT_OF_SERVICE_NOROAD_AROUND_END_POLYLINE= 10010;
    public static final int ERROR_CODE_ROUTE_BUS_FAILURE= 10180;
    public static final int ERROR_CODE_ROUTE_BUS_FAILURE1= 10182;
    public static final int ERROR_CODE_ROUTE_BUS_FAILURE2= 10183;
    public static final int ERROR_CODE_ROUTE_BUS_FAILURE3= 10184;
    public static final int ERROR_CODE_ID_NOT_FOUND = 46;
    public static final int ERROR_CODE_SCODE = 60;
    public static final int ERROR_CODE_NOSUCHALGORITHM = 61;
    public static final int ERROR_CODE_KEYMANAGEMENT = 62;




    public static final String ERROR_CLOUD_1_MESSAGE = "已存在相同名称的数据集";//3001
    public static final String ERROR_CLOUD_2_MESSAGE = "数据集不可操作";//2002
    public static final String ERROR_CLOUD_3_MESSAGE = "坐标格式错误";//400
    public static final String ERROR_CLOUD_4_MESSAGE = "data里的自定义字段不存在";//4003
    public static final String ERROR_CLOUD_5_MESSAGE = "系统异常";//500
    public static final String ERROR_CLOUD_6_MESSAGE = "data格式错误";//4001
    public static final String ERROR_CLOUD_7_MESSAGE = "参数错误";//2
    public static final String ERROR_CLOUD_8_MESSAGE = "数据ID不存在";//5001
    public static final String ERROR_CLOUD_9_MESSAGE = "数据集不存在";//2001
    public static final String ERROR_CLOUD_10_MESSAGE = "ids有数据ID不存在\"";//5002
    public static final String ERROR_CLOUD_11_MESSAGE = "删除数据（单条/批量）失败";//2、1001
    public static final String ERROR_CLOUD_12_MESSAGE = "服务器内部错误";//500、1002
    public static final String ERROR_SEARCH_INVALID_PARAMETER1 = "datasource不能为空!";
    public static final String ERROR_SEARCH_INVALID_PARAMETER2 = "query和type二者不可同时存在!";
    public static final String ERROR_SEARCH_INVALID_PARAMETER3 = "location和region二者不可同时存在!";
    public static final String ERROR_SEARCH_NODATA = "查询结果为空!";


    public static final int ERROR_CODE_CLOUD_7 =2;
    public static final int ERROR_CODE_CLOUD_9 =2001;
    public static final int ERROR_CODE_CLOUD_2 =2002;
    public static final int ERROR_CODE_CLOUD_1 =3001;
    public static final int ERROR_CODE_CLOUD_3 =400;
    public static final int ERROR_CODE_CLOUD_6 =4001;
    public static final int ERROR_CODE_CLOUD_4 =4003;
    public static final int ERROR_CODE_CLOUD_5 =500;
    public static final int ERROR_CODE_CLOUD_8 =5001;
    public static final int ERROR_CODE_CLOUD_10 =5002;
    public static final int ERROR_CODE_CLOUD_11 =1001;
    public static final int ERROR_CODE_CLOUD_12 =1002;
    public static final int ERROR_CODE_SEARCH_INVALID_PARAMETER1 = 6001;
    public static final int ERROR_CODE_SEARCH_INVALID_PARAMETER2 = 6002;
    public static final int ERROR_CODE_SEARCH_INVALID_PARAMETER3 = 6003;
    public static final int ERROR_CODE_SEARCH_NODATA = 6004;


	// 错误信息
	private String errorMessage = ERROR_UNKNOWN;

	// 错误代码
	private int errorCode;

	public UnistrongException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		initErrorMessageCode(errorMessage);
	}

	public UnistrongException() {
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	private void initErrorMessageCode(String errorMessage) {
		if (errorMessage.equals(ERROR_IO)) {
			errorCode = ERROR_CODE_IO;
		} else if (errorMessage.equals(ERROR_SOCKET)) {
			errorCode = ERROR_CODE_SOCKET;
		} else if (errorMessage.equals(ERROR_SOCKE_TIME_OUT)) {
			errorCode = ERROR_CODE_SOCKE_TIME_OUT;
		} else if (errorMessage.equals(ERROR_NULL_PARAMETER)) {
			errorCode = ERROR_CODE_NULL_PARAMETER;
		} else if (errorMessage.equals(ERROR_INVALID_PARAMETER)) {
			errorCode = ERROR_CODE_INVALID_PARAMETER;
		} else if (errorMessage.equals(ERROR_UNKNOW_HOST)) {
			errorCode = ERROR_CODE_UNKNOW_HOST;
		} else if (errorMessage.equals(ERROR_UNKNOW_SERVICE)) {
			errorCode = ERROR_CODE_UNKNOW_SERVICE;
		} else if (errorMessage.indexOf(ERROR_Available_key,0)!=-1) {
			errorCode = ERROR_CODE_Available_key;
		} else if (errorMessage.equals(ERROR_CONNECTION)) {
			errorCode = ERROR_CODE_CONNECTION;
		} else if (errorMessage.equals(ERROR_URL)) {
			errorCode = ERROR_CODE_URL;
		} else if (errorMessage.indexOf(ERROR_INVALID_AUTH,0)!=-1) {
			errorCode = ERROR_CODE_INVALID_AUTH;
		}else if(errorMessage.equals(ERROR_SERVICE)){
			errorCode = ERROR_CODE_SERVICE;
		} else if(errorMessage.indexOf(ERROR_REQUEST,0)!=-1){
            errorCode = ERROR_CODE_REQUEST;
        }else if(errorMessage.indexOf(ERROR_ROUTE_BUS_FAILURE,0)!=-1){
            errorCode = ERROR_CODE_ROUTE_BUS_FAILURE;
        }else if(errorMessage.indexOf(ERROR_ROUTE_BUS_FAILURE1,0)!=-1){
            errorCode = ERROR_CODE_ROUTE_BUS_FAILURE1;
        }else if(errorMessage.indexOf(ERROR_ROUTE_BUS_FAILURE2,0)!=-1){
            errorCode = ERROR_CODE_ROUTE_BUS_FAILURE2;
        } else if(errorMessage.indexOf(ERROR_OUT_OF_SERVICE,0)!=-1){
            errorCode = ERROR_CODE_OUT_OF_SERVICE;
        }else if(errorMessage.indexOf(ERROR_OUT_OF_SERVICE_END,0)!=-1){
            errorCode = ERROR_CODE_OUT_OF_SERVICE_END;
        } else if(errorMessage.indexOf(ERROR_REQUEST1,0)!=-1){
            errorCode = ERROR_CODE_REQUEST1;
        } else if(errorMessage.indexOf(ERROR_ENGINE,0)!=-1){
            errorCode = ERROR_CODE_ENGINE;
        }else if(errorMessage.indexOf(ERROR_ROUTE_BUS_FAILURE3,0)!=-1){
            errorCode = ERROR_CODE_ROUTE_BUS_FAILURE3;
        }else if(errorMessage.indexOf(ERROR_FAILURE_AUTH,0)!=-1){
            errorCode = ERROR_CODE_FAILURE_AUTH;
        }
        else if(errorMessage.indexOf(ERROR_CLOUD_1_MESSAGE,0)!=-1){
            errorCode= ERROR_CODE_CLOUD_1;
                }
        else if(errorMessage.contains(ERROR_CLOUD_2_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_2;
                }
        else if(errorMessage.contains(ERROR_CLOUD_3_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_3;
                }
        else if(errorMessage.contains(ERROR_CLOUD_4_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_4;
                }
        else if(errorMessage.contains(ERROR_CLOUD_5_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_5;
                }
        else if(errorMessage.contains(ERROR_CLOUD_6_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_6;
                }
        else if(errorMessage.contains(ERROR_CLOUD_7_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_7;
                }
        else if(errorMessage.contains(ERROR_CLOUD_8_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_8;
                }
        else if(errorMessage.contains(ERROR_CLOUD_9_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_9;
                }
        else if(errorMessage.contains(ERROR_CLOUD_10_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_10;
                }
        else if(errorMessage.contains(ERROR_CLOUD_12_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_11;
                }
        else if(errorMessage.contains(ERROR_CLOUD_11_MESSAGE)){
            errorCode= ERROR_CODE_CLOUD_12;
        } else if (errorMessage.contains(ERROR_SEARCH_INVALID_PARAMETER1)){
            errorCode = ERROR_CODE_SEARCH_INVALID_PARAMETER1;
        } else if (errorMessage.contains(ERROR_SEARCH_INVALID_PARAMETER2)){
            errorCode = ERROR_CODE_SEARCH_INVALID_PARAMETER2;
        } else if (errorMessage.contains(ERROR_SEARCH_INVALID_PARAMETER3)){
            errorCode = ERROR_CODE_SEARCH_INVALID_PARAMETER3;
        } else if (errorMessage.contains(ERROR_SEARCH_NODATA)){
            errorCode = ERROR_CODE_SEARCH_NODATA;
        } else if (errorMessage.contains(ERROR_NOSUCHALGORITHM)){
            errorCode = ERROR_CODE_NOSUCHALGORITHM;
        } else if (errorMessage.contains(ERROR_KEYMANAGEMENT)){
            errorCode = ERROR_CODE_KEYMANAGEMENT;
        }
	}
}
