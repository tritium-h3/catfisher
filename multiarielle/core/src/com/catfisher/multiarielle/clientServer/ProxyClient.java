package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.controller.EventConsumer;
import com.catfisher.multiarielle.controller.event.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Log4j2
public class ProxyClient implements EventConsumer<Boolean> {
    @Getter
    final String clientId;
    @Getter
    final ChannelHandlerContext ctx;

    @Override
    public Boolean consume(Event e) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String eventToSend = objectMapper.writeValueAsString(e);
            log.info("Sending {}", eventToSend);
            ctx.writeAndFlush(Unpooled.wrappedBuffer((eventToSend + "\r\n").getBytes(StandardCharsets.UTF_8))).sync();
            return true;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return false;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
