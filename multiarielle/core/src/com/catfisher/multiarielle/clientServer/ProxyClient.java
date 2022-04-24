package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.server.ServerEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedInput;
import io.netty.handler.stream.ChunkedStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Log4j2
public class ProxyClient {
    @Getter
    private final String clientId;
    @Getter
    private final ChannelHandlerContext ctx;
    @Getter
    private final AtomicLong sequenceNumberWatermark = new AtomicLong(0);

    public Boolean consume(ServerEvent e) {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

        try {
            String eventToSend = objectMapper.writeValueAsString(e);
            log.info("Sending {}", eventToSend);
            ctx.writeAndFlush(eventToSend + "\n");
            return true;
        } catch (JsonProcessingException ex) {
            log.error("Error parsing JSON from server", ex);
            return false;
        }
    }
}
