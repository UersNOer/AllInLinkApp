package com.example.android_supervisor.jt808;

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
    private MsgProcService msgProcService;

    private final String host;
    private final int port;

    private boolean manualLock;

    public GpsEchoClient(String host, int port) {
        this.host = host;
        this.port = port;

        bossGroup = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new InitialChannelHandler())
                .option(ChannelOption.SO_BACKLOG, 128);
    }

    public void connect() {
        try {
            ChannelFuture future = bootstrap.connect(host, port);
            future.addListener(connectListener);
            future.channel().closeFuture().addListener(closeListener);
        } catch (Exception e) {
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

        if (msgProcService != null) {
            msgProcService.sendGpsPoint(latitude, longitude, elevation, speed, direction, coordType, provider, inGrid, time, userId);
        }
    }

    public void disconnect() {
        if (msgProcService != null) {
            msgProcService.close();
            manualLock = true;
        }
    }

    class InitialChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        public void initChannel(SocketChannel channel) throws Exception {
            channel.pipeline().addLast("ping",
                    new IdleStateHandler(Constants.TCP_CLIENT_IDLE_MINUTES, Constants.TCP_CLIENT_IDLE_MINUTES, 0, TimeUnit.MINUTES));
            channel.pipeline().addLast(new LoggingHandler());

            // 1024表示单条消息的最大长度，解码器在查找分隔符的时候，达到该长度还没找到的话会抛异常
            channel.pipeline().addLast(
                    new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(new byte[]{0x7e}),
                            Unpooled.copiedBuffer(new byte[]{0x7e, 0x7e})));

            msgProcService = new MsgProcService(channel, "0000000000");
            channel.pipeline().addLast(new GpsClientHandler(msgProcService));
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
                        System.err.println("服务端连接不上，开始重连操作...");
                        connect();
                    }
                }, 5, TimeUnit.SECONDS);
            } else {
                System.err.println("服务端连接成功...");
            }
        }
    };

    ChannelFutureListener closeListener = new ChannelFutureListener() {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                System.out.println("连接已关闭..");
                if (!manualLock) {
                    final EventLoop loop = future.channel().eventLoop();
                    loop.submit(new Runnable() {
                        @Override
                        public void run() {
                            System.err.println("与服务端断开连接后，开始重连操作...");
                            connect();
                            manualLock = false;
                        }
                    });
                } else {
                    try {
                        bossGroup.shutdownGracefully().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}
