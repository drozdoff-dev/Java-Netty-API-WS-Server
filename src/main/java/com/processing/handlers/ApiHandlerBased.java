package com.processing.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public abstract class ApiHandlerBased {
    public abstract void onMessage(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff);
    public String getContentType() {
        return "text/plain; charset=UTF-8";
    }
}
