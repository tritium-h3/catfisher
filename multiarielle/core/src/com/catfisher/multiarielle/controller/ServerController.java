package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.clientServer.ProxyClient;
import com.catfisher.multiarielle.clientServer.event.ClientEvent;
import com.catfisher.multiarielle.clientServer.event.ConnectEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Log4j2
public class ServerController extends ChannelInboundHandlerAdapter {
    private final ModelServer server;
    private final int port;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {

        server.removeClient(ctx);
    }

    private void consume(ClientEvent e) {
        if (!server.consume(e)) {
            // TODO error handling
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String req = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();

        log.info("Message received");

        try {
            ClientEvent e = objectMapper.readValue(req, ClientEvent.class);
            ConnectEvent newConnect;
            log.info("Received event {}", e);
            if (e instanceof ConnectEvent) {
                newConnect = new ConnectEvent(e.getClientId(), ctx);
                consume(newConnect);
            } else {
                consume(e);
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ServerController(server, port));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
