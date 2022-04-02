package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.clientServer.event.ConnectEvent;
import com.catfisher.multiarielle.controller.delta.*;

public interface DeltaVisitor<Response> {
    Response visit(MoveDelta e);
    Response visit(CharacterAddDelta e);
    Response visit(SynchronizeDelta e);

    Response visit(ConnectEvent connectEvent);

    Response visit(CharacterRemoveDelta characterRemoveEvent);
}
