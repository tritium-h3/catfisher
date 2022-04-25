package com.catfisher.multiarielle.clientServer.event.client;

import com.catfisher.multiarielle.clientServer.event.client.ClientEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientEventVisitor;
import io.netty.channel.ChannelHandlerContext;
import lombok.*;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ConnectEvent extends ClientEvent {
    ChannelHandlerContext ctx;
    private final String password;

    @Override
    public <Response> Response receive(ClientEventVisitor<Response> visitor) {
        return visitor.visit(this);
    }

    public ConnectEvent(String clientId, String password, ChannelHandlerContext ctx) {
        super(clientId);
        this.password = password;
        this.ctx = ctx;
    }
}
