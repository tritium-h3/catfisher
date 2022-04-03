package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.server.ServerEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String eventToSend = objectMapper.writeValueAsString(e);
            log.info("Sending {}", eventToSend);
            ctx.writeAndFlush(Unpooled.wrappedBuffer((eventToSend + "\r\n").getBytes(StandardCharsets.UTF_8))).sync();
            return true;
        } catch (JsonProcessingException ex) {
            log.error("Error parsing JSON from server", ex);
            return false;
        } catch (InterruptedException ex) {
            log.error("Interrupted while sending message to client", ex);
            ex.printStackTrace();
            return false;
        }
    }
}
