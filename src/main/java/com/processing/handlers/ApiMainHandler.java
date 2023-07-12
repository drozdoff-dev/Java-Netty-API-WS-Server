package com.processing.handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.lang.annotation.Annotation;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

public class ApiMainHandler extends SimpleChannelInboundHandler<Object> {

    private HttpRequest request;
    private final StringBuilder buf = new StringBuilder();
    private Map<Entry<String, String>, ApiHandlerBased> api_handlers = new HashMap<Entry<String, String>, ApiHandlerBased>();

    private ChannelHandlerContext ctx;
    private Object msg;

    public ApiMainHandler(ChannelHandlerContext _ctx, Object _msg) {
        ctx = _ctx;
        msg = _msg;

        if (api_handlers.size()==0) {
            try {
                for (Class c : ReflectionTools.getClasses(getClass().getPackage().getName() + ".api")) {
                    Annotation annotation = c.getAnnotation(route.class);
                    if (annotation!=null) {
                        api_handlers.put(new AbstractMap.SimpleEntry(((route) annotation).uri(), ((route) annotation).method()), (ApiHandlerBased)c.newInstance());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        ApiHandlerBased api_handler = null;

        if (this.msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) this.msg;
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
            buf.setLength(0);
            String context = queryStringDecoder.path();
            String method = String.valueOf(request.method());
            api_handler = api_handlers.get(new AbstractMap.SimpleEntry(context, method));
            if (api_handler!=null) {
                api_handler.onMessage(this.ctx, this.msg, request, buf);
            }
            ctx.flush();
            ctx.close();
        } else {
            System.out.println("Incoming request is unknown");
        }
    }
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}