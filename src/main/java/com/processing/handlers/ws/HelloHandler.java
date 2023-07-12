package com.processing.handlers.ws;

import com.processing.handlers.WebSocketHandlerBased;
import com.processing.handlers.route;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.*;

@route(uri="/hello")
public class HelloHandler extends WebSocketHandlerBased {
    public void onMessageBinary(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff) {

    }
    public void onMessageText(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff) {

    }
    public void onPing(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff) {

    }
    public void onPong(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff) {

    }
    public void onClose(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff) {
        System.out.println ("«Соединение между клиентом и сервером закрыто»");
    }
    public void onInactive(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff) {
        System.out.println ("«Соединение между клиентом и сервером закрыто»");
    }
}
