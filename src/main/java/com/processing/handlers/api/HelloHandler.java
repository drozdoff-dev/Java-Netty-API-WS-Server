package com.processing.handlers.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.processing.handlers.ApiHandlerBased;
import com.processing.handlers.route;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@route(method="POST", uri="/hello")
public class HelloHandler extends ApiHandlerBased {
    public void onMessage(ChannelHandlerContext ctx, Object msg, HttpRequest request, StringBuilder buff) {
        JsonObject json = new JsonObject();
        JsonObject jsonarr = new JsonObject();
        JsonArray arr = new JsonArray();
        arr.add("hello");
        arr.add("world");
        jsonarr.addProperty("one", 1);
        jsonarr.addProperty("two", 2);
        jsonarr.addProperty("three", 3);
        arr.add(jsonarr);
        json.addProperty("testing", "tess");
        json.add("testing1", arr);

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer(json.toString(), StandardCharsets.UTF_8));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response .content().readableBytes());
        ctx.write(response);
        ctx.flush();
    }
}
