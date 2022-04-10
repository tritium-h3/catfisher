package com.catfisher.multiarielle.clientServer;


import com.catfisher.multiarielle.clientServer.event.client.ClientEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@Log4j2
public class ProxyServer extends SimpleChannelInboundHandler<String> {

    ModelClient client;
    ChannelHandlerContext conn;

    public void setupClient(ModelClient client) {
        this.client = client;
    }

    public boolean receive(ClientEvent e) {
        synchronized (this) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serialized = objectMapper.writeValueAsString(e);
                log.debug("Serialized into {}", serialized);
                conn.writeAndFlush(serialized).sync();
            } catch (JsonProcessingException ex) {
                log.error("Json processing exception", ex);
            } catch (InterruptedException ex) {
                log.error("Interrupted", ex);
            }
        }
        return true;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        conn = ctx;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ServerEvent event = objectMapper.readValue(msg, ServerEvent.class);
            log.info("Event received: {}", event);

            client.consume(event);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Network error", cause);
        ctx.close();
    }

}
