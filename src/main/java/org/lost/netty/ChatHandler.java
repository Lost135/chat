package org.lost.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //保存所有的用户端连接
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:MM");

    //当channel中有新的事件消息时调用
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //接受到数据后自动调用
        //获取文本消息
        String text = textWebSocketFrame.text();
        System.out.println("######"+text+"######");

        //将消息发送给所有客户
        for(Channel client:clients){
            client.writeAndFlush(new TextWebSocketFrame(dateFormat.format(new Date()) + " : " + text));
        }
    }

    //当有新用户连接时调用
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }
}
