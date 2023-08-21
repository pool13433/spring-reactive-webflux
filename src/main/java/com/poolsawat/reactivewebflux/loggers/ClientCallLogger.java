package com.poolsawat.reactivewebflux.loggers;

import com.poolsawat.reactivewebflux.constants.HeaderKeys;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.LastHttpContent.EMPTY_LAST_CONTENT;

@Component
@Slf4j
public class ClientCallLogger extends LoggingHandler {

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    private void log(String content) {
        if (content != null)
            log.info(content);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log(format(ctx, "READ", msg));
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log(format(ctx, "WRITE", msg));
        super.write(ctx, msg, promise);
    }

    @Override
    protected String format(ChannelHandlerContext ctx, String eventName, Object arg) {
        if (arg instanceof ByteBuf) {
            return super.format(ctx, eventName, arg);
        } else {
            if (arg instanceof HttpRequest i) {
                return formatRequestHeadersAndBody((HttpRequest) arg);
            } else if (arg instanceof HttpResponse l) {
                return formatResponseMetaData((HttpResponse) arg);
            } else if (arg instanceof ByteBufHolder d) {
                return formatResponseBody((ByteBufHolder) arg);
            } else {
                return super.format(ctx, eventName, arg);
            }
        }
    }

    private String tryExtractBody(Object arg) {
        try {
           return ((ByteBufHolder)arg).content().toString(StandardCharsets.UTF_8);
        } catch (Exception ex ) {
            log.warn("error occurred while extracting body from http, ignored with empty", ex);
            return "";
        }
    }
    private String formatRequestHeadersAndBody(HttpRequest req) {
        val builder = new StringBuilder("HTTPClient Request    --> method=[");
         if (req instanceof DefaultFullHttpRequest) {
             return builder.append(req.method()).append(HeaderKeys.URI.getKey()).append(req.uri()).append(HeaderKeys.HEADERS.getKey()).append(req.headers())
                    .append("] body=[").append(tryExtractBody(req)).append("]").toString();
        } else {
             return builder.append(req.method()).append(HeaderKeys.URI.getKey()).append(req.uri()).append(HeaderKeys.HEADERS.getKey()).append(req.headers())
                     .append("]").toString();
         }
    }

    private String formatResponseMetaData(HttpResponse res) {
        return new StringBuilder("HTTPClient Response Meta Data  <-- status=[").append(res.status()).append(HeaderKeys.HEADERS.getKey())
                .append(res.headers()).append("]").toString();
    }

    private String formatResponseBody(ByteBufHolder res){
        if (res == EMPTY_LAST_CONTENT)
            return null;
        return new StringBuilder("HTTPClient Response Body  <-- body=[").append(tryExtractBody(res)).append("]").toString();
    }


}