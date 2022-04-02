package com.catfisher.multiarielle.clientServer.event;

import com.catfisher.multiarielle.controller.DeltaVisitor;
import com.catfisher.multiarielle.controller.delta.Delta;
import io.netty.channel.ChannelHandlerContext;
import lombok.*;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ConnectEvent extends ClientEvent {
    ChannelHandlerContext ctx;

    @Override
    public <Response> Response receive(ClientEventVisitor<Response> visitor) {
        return visitor.visit(this);
    }

    public ConnectEvent(String clientId, ChannelHandlerContext ctx) {
        super(clientId);
        this.ctx = ctx;
    }
}
