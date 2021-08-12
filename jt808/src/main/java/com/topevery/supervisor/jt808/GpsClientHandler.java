package com.example.android_supervisor.jt808;

import com.example.android_supervisor.jt808.bins.MsgHeader;
import com.example.android_supervisor.jt808.bins.RespMsgBody;
import com.example.android_supervisor.jt808.utils.JT808Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class GpsClientHandler extends ChannelInboundHandlerAdapter {
	private MsgProcService msgProcService;

	public GpsClientHandler(MsgProcService msgProcService) {
		this.msgProcService = msgProcService;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException { // (2)
		try {
			ByteBuf buffer = (ByteBuf) msg;
			if (buffer.readableBytes() <= 0) {
				return;
			}
			byte[] bytes = JT808Utils.parseMsg(buffer);

			MsgHeader msgHeader = new MsgHeader(bytes);
			final int msgId = msgHeader.getMsgId();
			final int msgHeadLen = msgHeader.getByteSize();
			final int msgBodyLen = msgHeader.getMsgBodyLength();

			if (msgBodyLen > 0) {
				byte[] bodyBytes = new byte[msgBodyLen];
				System.arraycopy(bytes, msgHeadLen, bodyBytes, 0, msgBodyLen);

				RespMsgBody respMsgBody = new RespMsgBody(bodyBytes);
				if (respMsgBody.getReplyCode() == RespMsgBody.SUCCESS) {
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ReferenceCountUtil.release(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		msgProcService.auth();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.WRITER_IDLE) {
				msgProcService.heartBeat();
			} else if (event.state() == IdleState.READER_IDLE) {
				ctx.channel().close();
			}
		}
	}
}