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
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@Log4j2
public class ProxyServer extends ChannelInboundHandlerAdapter {

    ModelClient client;
    ChannelHandlerContext conn;

    public void setupClient(ModelClient client) {
        this.client = client;
    }

    public boolean receive(ClientEvent e) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serialized = objectMapper.writeValueAsString(e);
            log.info("Serialized into {}", serialized);
            conn.writeAndFlush(Unpooled.wrappedBuffer((serialized + "\r\n").getBytes(StandardCharsets.UTF_8))).sync();
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        conn = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        String messageStr = m.toString(StandardCharsets.UTF_8);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ServerEvent event = objectMapper.readValue(messageStr, ServerEvent.class);
            log.info("Event received: {}", event);

            client.consume(event);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            m.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Network error", cause);
        ctx.close();
    }

}
