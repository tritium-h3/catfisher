package com.catfisher.multiarielle.clientServer.event;

public interface ServerEventVisitor<Response> {
    Response visit(ServerDeltaEvent e);
}
