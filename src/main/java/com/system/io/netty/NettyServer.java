package com.system.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author wangyh
 * @create 2020-09-10 20:14
 */

public class NettyServer {
    private static final int port = 9099;

    public static void main(String[] args) throws Exception {
        start();
    }


    public static void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(6);
        ServerBootstrap bootstrap = new ServerBootstrap();
        System.out.println("netty server start");
        try {
            bootstrap
                    .group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class)// channel的实现
                    .option(ChannelOption.SO_BACKLOG, 1024)//初始化服务器连接队列大小，服务端处理客户端连接请求是顺序处理的,所以同一时间只能处理一个客户端连接。
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //初始化通道对象，设置初始化参数
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture cf = bootstrap.bind(port).sync();
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
