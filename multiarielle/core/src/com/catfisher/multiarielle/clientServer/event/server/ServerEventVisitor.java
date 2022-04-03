package com.catfisher.multiarielle.clientServer.event.server;

import com.catfisher.multiarielle.clientServer.event.server.ServerDeltaEvent;

public interface ServerEventVisitor<Response> {
    Response visit(ServerDeltaEvent e);
    Response visit(SynchronizeEvent e);
}
