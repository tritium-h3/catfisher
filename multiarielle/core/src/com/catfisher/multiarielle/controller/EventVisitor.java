package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.controller.event.*;

public interface EventVisitor<Response> {
    Response visit(MoveEvent e);
    Response visit(CharacterAddEvent e);
    Response visit(SynchronizeEvent e);

    Response visit(ConnectEvent connectEvent);

    Response visit(CharacterRemoveEvent characterRemoveEvent);
}
