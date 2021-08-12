package com.example.android_supervisor.jt808;

public class Constants {

	public static final String STRING_ENCODING = "GBK";

	// 标识位
	public static final int DELIMITER = 0x7e;

	// 客户端心跳包间隔
	public static int TCP_CLIENT_IDLE_MINUTES = 5;

	// 终端通用应答
	public static final int MSG_ID_TERMINAL_COMMON_RESP = 0x0001;
	// 终端心跳
	public static final int MSG_ID_HEART_BEAT = 0x0002;
	// 终端注册
	public static final int MSG_ID_REGISTER = 0x0100;
	// 终端注销
	public static final int MSG_ID_LOG_OUT = 0x0003;
	// 终端鉴权
	public static final int MSG_ID_AUTH = 0x0102;
	// 位置信息汇报
	public static final int MSG_ID_GPS_UPLOAD = 0x0201;
	// 查询终端参数应答
	public static final int MSG_ID_PARAM_QUERY_RESP = 0x0104;

	// 平台通用应答
	public static final int CMD_COMMON_RESP = 0x8001;
	// 终端注册应答
	public static final int CMD_REGISTER_RESP = 0x8100;
	// 设置终端参数
	public static final int CMD_PARAM_SETTINGS = 0X8103;
	// 查询终端参数
	public static final int CMD_PARAM_QUERY = 0x8104;

}
