package com.catfisher.multiarielle.clientServer.event.client;

public interface ClientEventVisitor<Response> {
    Response visit(ConnectEvent e);
    Response visit(ClientDeltaEvent e);
    Response visit(ClientChatEvent e);
}
