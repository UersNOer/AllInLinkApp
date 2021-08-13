package com.example.android_supervisor.jt808.bins;

public class RespMsgBody extends Binary {
	public static final byte SUCCESS = 0;
	public static final byte FAILURE = 1;
	public static final byte MSG_ERROR = 2;
	public static final byte UNSUPPORTED = 3;
	public static final byte WARNNING_MSG_ACK = 4;

	// 应答流水号 对应的终端消息的流水号
	private final Int16 replyFlowId = new Int16();

	// 应答ID 对应的终端消息的ID
	private final Int16 replyId = new Int16();

	// 0：成功/确认；1：失败；2：消息有误；3：不支持；4：报警处理确认
	private final Int8 replyCode = new Int8();

	public RespMsgBody() {
	}

	public RespMsgBody(byte[] bytes) {
		super(bytes);
	}

	public int getReplyFlowId() {
		return replyFlowId.get();
	}

	public void setReplyFlowId(int flowId) {
		this.replyFlowId.set(flowId);
	}

	public int getReplyId() {
		return replyId.get();
	}

	public void setReplyId(int msgId) {
		this.replyId.set(msgId);
	}

	public int getReplyCode() {
		return replyCode.get();
	}

	public void setReplyCode(byte code) {
		this.replyCode.set(code);
	}
}
