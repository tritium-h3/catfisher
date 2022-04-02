package com.catfisher.multiarielle.clientServer.event;

public interface ClientEventVisitor<Response> {
    Response visit(ConnectEvent e);
    Response visit(ClientDeltaEvent e);
}
