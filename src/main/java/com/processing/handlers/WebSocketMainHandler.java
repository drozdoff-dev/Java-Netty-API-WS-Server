package com.processing.handlers;

import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.*;

import java.lang.annotation.Annotation;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebSocketMainHandler extends SimpleChannelInboundHandler<Object> {
    private HttpRequest request;
    private final StringBuilder buf = new StringBuilder();
    private Map<String, WebSocketHandlerBased> ws_handlers = new HashMap<String, WebSocketHandlerBased>();

    private ChannelHandlerContext ctx;
    private Object msg;

    public WebSocketMainHandler(ChannelHandlerContext _ctx, Object _msg) {
        ctx = _ctx;
        msg = _msg;

        if (ws_handlers.size()==0) {
            try {
                for (Class c : ReflectionTools.getClasses(getClass().getPackage().getName() + ".ws")) {
                    Annotation annotation = c.getAnnotation(route.class);
                    if (annotation!=null) {
                        ws_handlers.put(((route) annotation).uri(), (WebSocketHandlerBased)c.newInstance());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        // Добавить в
//        System.out.println ("«Соединение между клиентом и сервером открыто»");
//    }
//
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // удалять
        WebSocketHandlerBased ws_handler = null;

        HttpRequest request = this.request = (HttpRequest) this.msg;
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        buf.setLength(0);

        String context = queryStringDecoder.path();
        ws_handler = ws_handlers.get(context);

        if (ws_handler!=null) {
            ws_handler.onInactive(this.ctx, this.msg, request, buf);
        }
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object _msg) {
        WebSocketHandlerBased ws_handler = null;

        WebSocketFrame msg = (WebSocketFrame) _msg;

        if (msg instanceof WebSocketFrame) {
            HttpRequest request = this.request = (HttpRequest) this.msg;
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
            buf.setLength(0);

            String context = queryStringDecoder.path();
            ws_handler = ws_handlers.get(context);

            System.out.println("This is a WebSocket frame");
            System.out.println("Client Channel : " + ctx.channel());
            if (msg instanceof BinaryWebSocketFrame) {
                System.out.println("BinaryWebSocketFrame Received : ");

                if (ws_handler!=null) {
                    ws_handler.onMessageBinary(this.ctx, this.msg, request, buf);
                }
            } else if (msg instanceof TextWebSocketFrame) {
                System.out.println("TextWebSocketFrame Received : ");

                if (ws_handler!=null) {
                    ws_handler.onMessageText(this.ctx, this.msg, request, buf);
                }
            } else if (msg instanceof PingWebSocketFrame) {
                System.out.println("PingWebSocketFrame Received : ");
                ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
                if (ws_handler!=null) {
                    ws_handler.onPing(this.ctx, this.msg, request, buf);
                }
                return;
            } else if (msg instanceof PongWebSocketFrame) {
                System.out.println("PongWebSocketFrame Received : ");

                if (ws_handler!=null) {
                    ws_handler.onPong(this.ctx, this.msg, request, buf);
                }
            } else if (msg instanceof CloseWebSocketFrame) {
                if (ws_handler!=null) {
                    ws_handler.onClose(this.ctx, this.msg, request, buf);
                }
                ctx.close();
                System.out.println("CloseWebSocketFrame Received : ");
                System.out.println("ReasonText :" + ((CloseWebSocketFrame) msg).reasonText());
                System.out.println("StatusCode : " + ((CloseWebSocketFrame) msg).statusCode());
            } else {
                System.out.println("Unsupported WebSocketFrame");

                //ctx.writeAndFlush(new TextWebSocketFrame("Server side Message received, message:"));

            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void sendMessage(ChannelHandlerContext ctx, JsonObject message) {
        ctx.writeAndFlush(new TextWebSocketFrame(message.toString()));
    }
}