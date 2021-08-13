package com.example.android_supervisor.jt808;

import android.util.Log;

import com.example.android_supervisor.jt808.bins.AuthMsgBody;
import com.example.android_supervisor.jt808.bins.GpsMsgBody;
import com.example.android_supervisor.jt808.bins.LocMsgBody;
import com.example.android_supervisor.jt808.bins.MsgHeader;
import com.example.android_supervisor.jt808.bins.RegMsgBody;
import com.example.android_supervisor.jt808.utils.JT808Utils;

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

    private String LOG_TAG = "JT808-Channel";

    public MsgProcService(Channel channel, String phoneNumber) {
        this.channel = channel;
        this.phoneNumber = phoneNumber;
    }

    public void heartBeat() {
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgId(Constants.MSG_ID_HEART_BEAT);
        msgHeader.setSerialId(serialId.getAndIncrement());
        msgHeader.setPhoneNumber(phoneNumber);
        msgHeader.setMsgBodyInfo(0, 0b000, 1);

        byte[] bytes = JT808Utils.pkgMsg(msgHeader.toByteArray(), new byte[0]);
        try {
            ChannelFuture future = channel.writeAndFlush(bytes).sync();
            if (!future.isSuccess()) {
                Log.e(LOG_TAG, "发送数据出错：" + future.cause());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        RegMsgBody msgBody = new RegMsgBody();
        msgBody.setLicensePlate("360313199106270019");

        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgId(Constants.MSG_ID_REGISTER);
        msgHeader.setSerialId(serialId.getAndIncrement());
        msgHeader.setPhoneNumber(phoneNumber);
        msgHeader.setMsgBodyInfo(msgBody.getByteSize(), 0b000, 1);

        byte[] bytes = JT808Utils.pkgMsg(msgHeader.toByteArray(), msgBody.toByteArray());
        try {
            ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
            if (!future.isSuccess()) {
                Log.e(LOG_TAG, "发送数据出错：" + future.cause());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void auth() {
        AuthMsgBody msgBody = new AuthMsgBody();
        msgBody.setAuthCode("android");

        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgId(Constants.MSG_ID_AUTH);
        msgHeader.setSerialId(serialId.getAndIncrement());
        msgHeader.setPhoneNumber(phoneNumber);
        msgHeader.setMsgBodyInfo(msgBody.getByteSize(), 0b000, 1);

        byte[] bytes = JT808Utils.pkgMsg(msgHeader.toByteArray(), msgBody.toByteArray());
        try {
            ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
            if (!future.isSuccess()) {
                Log.e(LOG_TAG, "发送数据出错：" + future.cause());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
            if (!future.isSuccess()) {
                Log.e(LOG_TAG, "发送数据出错：" + future.cause());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (channel.isOpen()) {
            channel.disconnect();
            channel.close();
        }
    }
}
