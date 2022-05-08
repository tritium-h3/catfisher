package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.clientServer.event.client.ClientEvent;
import com.catfisher.multiarielle.clientServer.event.client.ConnectEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerConnectionRejected;
import com.catfisher.multiarielle.util.LineAccumulator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Log4j2
public class ServerController extends SimpleChannelInboundHandler<String> {
    private final ModelServer server;
    private final int port;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LineAccumulator lineAccumulator = new LineAccumulator();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {

        server.removeClient(ctx);
    }

    private boolean consume(ClientEvent e) {
        return server.consume(e);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        try {
            lineAccumulator.addChunk(msg);
            for (String line : lineAccumulator.getCompletedLines()) {
                ClientEvent e = objectMapper.readValue(line, ClientEvent.class);
                ConnectEvent newConnect;
                log.info("Received event {}", e);
                if (e instanceof ConnectEvent) {
                    newConnect = new ConnectEvent(e.getClientId(), ((ConnectEvent) e).getPassword(), ctx);
                    if (!consume(newConnect)) {
                        String toSend = objectMapper.writeValueAsString(new ServerConnectionRejected("Username already logged in."));
                        ctx.writeAndFlush(toSend + "\0");
                    }
                } else {
                    consume(e);
                }
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
                            ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                            ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
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
