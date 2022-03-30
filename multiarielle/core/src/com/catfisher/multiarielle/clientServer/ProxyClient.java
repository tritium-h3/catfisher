package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.controller.EventConsumer;
import com.catfisher.multiarielle.controller.event.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProxyClient implements EventConsumer<Boolean> {
    @Getter
    final String clientId;
    final ChannelHandlerContext ctx;

    @Override
    public Boolean consume(Event e) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String eventToSend = objectMapper.writeValueAsString(e);
            ctx.writeAndFlush(eventToSend);
            return true;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
