package com.example.android_supervisor.jt808;

import android.content.Context;

import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author wujie
 */
public class GpsEchoClient {
    private NioEventLoopGroup bossGroup;
    private Bootstrap bootstrap;
    private ChannelFuture connectionFuture;
    private MsgProcService msgProcService;

    private Context context;
    private final String host;
    private final int port;
    private final String userId;
    private final String phone;
    private boolean disconnected;

    public GpsEchoClient(Context context, String host, int port) {
        this.context = context;
        this.host = host;
        this.port = port;
        this.userId = UserSession.getUserId(context);
        this.phone = UserSession.getMobile(context);

        bossGroup = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new InitialChannelHandler())//指定编解码器，处理数据的Handler
                .option(ChannelOption.SO_BACKLOG, 128);
    }

    public void connect() {
        if (connectionFuture != null) {
            connectionFuture.removeListener(connectListener);
            connectionFuture = null;
        }
        try {
            connectionFuture = bootstrap.connect(host, port);
            connectionFuture.addListener(connectListener);
            connectionFuture.channel().closeFuture().addListener(closeListener);
            disconnected = false;
        } catch (Exception e) {
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
                             Date time) {

        if (msgProcService != null) {
            msgProcService.sendGpsPoint(latitude, longitude, elevation, speed,
                    direction, coordType, provider, inGrid, time, userId);
        }
    }

    public void disconnect() {
        disconnected = true;
        if (msgProcService != null) {
            msgProcService.close();
            msgProcService = null;
        }
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void heartBeat() {
        if (msgProcService != null) {
            msgProcService.heartBeat();
        }
    }

    class InitialChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        public void initChannel(SocketChannel channel) throws Exception {
            channel.pipeline().addLast("ping",
                    new IdleStateHandler(5, 1, 0, TimeUnit.MINUTES));
            channel.pipeline().addLast(new LoggingHandler());

            // 1024表示单条消息的最大长度，解码器在查找分隔符的时候，达到该长度还没找到的话会抛异常
            channel.pipeline().addLast(
                    new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(new byte[]{0x7e}),
                            Unpooled.copiedBuffer(new byte[]{0x7e, 0x7e})));

            msgProcService = new MsgProcService(channel, phone);
            channel.pipeline().addLast(new GpsClientHandler(GpsEchoClient.this, msgProcService));
        }
    }

    ChannelFutureListener connectListener = new ChannelFutureListener() {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                final EventLoop loop = future.channel().eventLoop();
                loop.schedule(new Runnable() {
                    @Override
                    public void run() {
                        Logger.w("JT808: Not connected server, retry..");
                        connect();
                    }
                }, 5, TimeUnit.SECONDS);
            } else {
                Logger.i("JT808: Connect server successful!");
                NotifyManager.notify(context, Notifies.NOTIFY_TYPE_GPS, Notifies.NOTIFY_STATUS_ONLINE, 0);
                msgProcService.auth(userId);
            }
        }
    };

    ChannelFutureListener closeListener = new ChannelFutureListener() {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                Logger.w("JT808: The connection is closed!");
                NotifyManager.notify(context, Notifies.NOTIFY_TYPE_GPS, Notifies.NOTIFY_STATUS_OFFLINE, 0);
            }
        }
    };
}
