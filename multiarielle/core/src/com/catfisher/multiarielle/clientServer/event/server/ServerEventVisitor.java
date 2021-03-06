package com.catfisher.multiarielle.clientServer.event.server;

import com.catfisher.multiarielle.clientServer.event.server.ServerDeltaEvent;

public interface ServerEventVisitor<Response> {
    Response visit(ServerDeltaEvent e);
    Response visit(SynchronizeEvent e);
    Response visit(ServerConnectionAcknowledged e);
    Response visit(ServerRejectDeltaEvent e);
    Response visit(ServerChatEvent e);
    Response visit(ServerConnectionRejected e);
}
