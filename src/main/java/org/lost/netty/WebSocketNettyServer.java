package org.lost.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebSocketNettyServer {
    public static void main(String[] args) {
        NioEventLoopGroup mainGrp = new NioEventLoopGroup(); //创建主线程池
        NioEventLoopGroup subGrp = new NioEventLoopGroup(); //创建从线程池

        try {
            //创建Netty服务器启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //初始化服务器对象
            serverBootstrap
                    //指定线程池
                    .group(mainGrp,subGrp)
                    //指定netty通道类型
                    .channel(NioServerSocketChannel.class)
                    //指定通道初始化器用以加载当channel收到事件后如何处理业务
                    .childHandler(new WebSocketChannelInitializer());

            //绑定服务器端口，以同步方式启动服务器
            ChannelFuture future = serverBootstrap.bind(8080).sync();
            //等待服务器关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅关闭服务器
            mainGrp.shutdownGracefully();
            subGrp.shutdownGracefully();
        }
    }
}
