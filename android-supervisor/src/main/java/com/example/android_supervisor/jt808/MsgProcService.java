package com.example.android_supervisor.jt808;

import com.example.android_supervisor.jt808.bins.AuthMsgBody;
import com.example.android_supervisor.jt808.bins.GpsMsgBody;
import com.example.android_supervisor.jt808.bins.MsgHeader;
import com.example.android_supervisor.jt808.bins.RegMsgBody;
import com.example.android_supervisor.jt808.utils.JT808Utils;
import com.example.android_supervisor.utils.LogUtils;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * @author wujie
 */
public class MsgProcService {
    private Channel channel;
    private String phoneNumber;
    private AtomicInteger serialId = new AtomicInteger(0);

    public MsgProcService(Channel channel, String phoneNumber) {
        this.channel = channel;
        this.phoneNumber = phoneNumber;
    }

    /**
     * 发送心跳包  java.lang.UnsupportedOperationException: unsupported message type: [B (expected: ByteBuf, FileRegion)
     */
    public void heartBeat() {

//        AuthMsgBody msgBody = new AuthMsgBody();
//        msgBody.setAuthCode("android");
//        msgBody.setUserId("258258");

        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgId(Constants.MSG_ID_HEART_BEAT);
        msgHeader.setSerialId(serialId.getAndIncrement());
        msgHeader.setPhoneNumber(phoneNumber);
        msgHeader.setMsgBodyInfo(0, 0b000, 1);

        byte[] bytes = JT808Utils.pkgMsg(msgHeader.toByteArray(),new byte[0]);
        LogUtils.d("jt808心跳:"+bytes);
//        ByteBuf byteBuf = new ByteBuf();
        try {
            if (channel.isOpen() && channel.isWritable() && channel.isActive()) {
                ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
                if (future.isSuccess()) {
                    Logger.i("JT808: Send heart beat successful");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void register() {
        RegMsgBody msgBody = new RegMsgBody();

        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgId(Constants.MSG_ID_REGISTER);
        msgHeader.setSerialId(serialId.getAndIncrement());
        msgHeader.setPhoneNumber(phoneNumber);
        msgHeader.setMsgBodyInfo(msgBody.getByteSize(), 0b000, 1);

        byte[] bytes = JT808Utils.pkgMsg(msgHeader.toByteArray(), msgBody.toByteArray());
        try {
            if (channel.isOpen() && channel.isWritable() && channel.isActive()) {
                ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
                if (future.isSuccess()) {
                    Logger.i("JT808: Register successful!");
                } else {
                    Logger.i("JT808: Register failure");
                }
            }
        } catch (InterruptedException e) {
            Logger.e(e, "");
        }
    }

    public void auth(String userId) {
        AuthMsgBody msgBody = new AuthMsgBody();
        msgBody.setAuthCode("android");
        msgBody.setUserId(userId);

        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgId(Constants.MSG_ID_AUTH);
        msgHeader.setSerialId(serialId.getAndIncrement());
        msgHeader.setPhoneNumber(phoneNumber);
        msgHeader.setMsgBodyInfo(msgBody.getByteSize(), 0b000, 1);

        byte[] bytes = JT808Utils.pkgMsg(msgHeader.toByteArray(), msgBody.toByteArray());
        try {
            if (channel.isOpen() && channel.isWritable() && channel.isActive()) {
                ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
                if (future.isSuccess()) {
                    Logger.i("JT808: Auth successful!");
                } else {
                    Logger.w("JT808: Auth failure");
                }
            }
        } catch (InterruptedException e) {
            Logger.e(e, "");
        }
    }

    public void sendGpsPoint(float latitude,
                             float longitude,
                             int elevation,
                             float speed,
                             int direction,
                             int coordType,
                             int provider,
                             boolean inGrid,
                             Date time,
                             String userId) {
        GpsMsgBody msgBody = new GpsMsgBody();
        msgBody.setLatitude((int) (latitude * 1e6));
        msgBody.setLongitude((int) (longitude * 1e6));
        msgBody.setElevation(elevation);
        msgBody.setSpeed(speed);
        msgBody.setDirection(direction);
        msgBody.setCoordType(coordType);
        msgBody.setProvider(provider);
        msgBody.setInGrid(inGrid);
        msgBody.setTime(time);
        msgBody.setUserId(userId);

        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgId(Constants.MSG_ID_GPS_UPLOAD);
        msgHeader.setSerialId(serialId.getAndIncrement());
        msgHeader.setPhoneNumber(phoneNumber);
        msgHeader.setMsgBodyInfo(msgBody.getByteSize(), 0b000, 1);

        byte[] bytes = JT808Utils.pkgMsg(msgHeader.toByteArray(), msgBody.toByteArray());
        try {
            if (channel.isOpen() && channel.isWritable() && channel.isActive()) {
                ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
                if (future.isSuccess()) {
                    Logger.i("JT808: Send gps successful!");
                } else {
                    Logger.w("JT808: Send gps failure");
                }
            }
        } catch (InterruptedException e) {
            Logger.e(e, "");
        }
    }

    public void close() {
        if (channel.isOpen()) {
            channel.disconnect();
            channel.close();
        }
    }
}
