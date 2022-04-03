package com.catfisher.multiarielle.clientServer.event.server;

public class ServerConnectionAcknowledged extends ServerEvent {
    @Override
    public <Response> Response receive(ServerEventVisitor<Response> visitor) {
        return visitor.visit(this);
    }
}
