package com.processing.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public abstract class WebSocketHandlerBased {

    //public abstract void process(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff);
    public abstract void onMessageText(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff);
    public abstract void onMessageBinary(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff);
    public abstract void onPing(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff);
    public abstract void onPong(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff);
    public abstract void onClose(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff);
    public abstract void onInactive(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff);
    public String getContentType() {
        return "text/plain; charset=UTF-8";
    }
}
