package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.clientServer.event.client.ConnectEvent;
import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.controller.delta.*;

public interface DeltaVisitor<Response> {
    Response visit(MoveDelta e);
    Response visit(CharacterAddDelta e);
    Response visit(CharacterRemoveDelta characterRemoveEvent);
}
