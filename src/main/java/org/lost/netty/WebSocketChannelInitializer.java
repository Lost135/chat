package org.lost.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 通道初始化器
 * 加载通道处理器（channelHandler）
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //获取管道，将每个channelHandel添加到管道中
        ChannelPipeline pipeline = socketChannel.pipeline();
        //添加http编解码器
        pipeline.addLast(new HttpServerCodec());
        //添加大数据流支持
        pipeline.addLast(new ChunkedWriteHandler());
        //添加HttpRequest/Response聚合器
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
        //指定接受url路由
        pipeline.addLast(new WebSocketServerProtocolHandler("/lost"));

        //添加自定义handler
        pipeline.addLast(new ChatHandler());

    }
}
