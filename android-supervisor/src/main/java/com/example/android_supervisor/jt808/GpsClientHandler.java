package com.example.android_supervisor.jt808;

import com.example.android_supervisor.jt808.bins.MsgHeader;
import com.example.android_supervisor.jt808.bins.RespMsgBody;
import com.example.android_supervisor.jt808.utils.JT808Utils;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class GpsClientHandler extends ChannelInboundHandlerAdapter {
    private GpsEchoClient gpsEchoClient;
    private MsgProcService msgProcService;

    public GpsClientHandler(GpsEchoClient gpsEchoClient, MsgProcService msgProcService) {
        this.gpsEchoClient = gpsEchoClient;
        this.msgProcService = msgProcService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException { // (2)
        try {
            ByteBuf buffer = (ByteBuf) msg;
            if (buffer.readableBytes() <= 0) {
                Logger.w("JT808_channelRead: bufferSize=0");
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
                    Logger.i("JT808_channelRead: replyId=%d, replyFlowId=%d", respMsgBody.getReplyId(), respMsgBody.getReplyFlowId());
                } else {
                    Logger.w("JT808_channelRead: replyCode=%d", respMsgBody.getReplyCode());
                }
            }
        } catch (Exception e) {
            Logger.e(e, "");
        } finally {
            try {
                ReferenceCountUtil.release(msg);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logger.e(cause, "");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Logger.i("JT808_channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Logger.i("JT808_channelInactive");
        if (!gpsEchoClient.isDisconnected()) {
            //重新连接服务器
            ctx.channel().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    gpsEchoClient.connect();
                }
            }, 5, TimeUnit.SECONDS);
        }
        ctx.close();
    }

    //指定时间内没有write事件，则会触发userEventTriggered方法(心跳超时事件)
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        Logger.i("JT808_userEventTriggered");
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                msgProcService.heartBeat();
            } else if (event.state() == IdleState.READER_IDLE) {
                ctx.close();
            }
        }
    }
}