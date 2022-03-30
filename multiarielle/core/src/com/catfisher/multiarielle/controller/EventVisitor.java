package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.controller.event.CharacterAddEvent;
import com.catfisher.multiarielle.controller.event.ConnectEvent;
import com.catfisher.multiarielle.controller.event.MoveEvent;
import com.catfisher.multiarielle.controller.event.SynchronizeEvent;

public interface EventVisitor<Response> {
    Response visit(MoveEvent e);
    Response visit(CharacterAddEvent e);
    Response visit(SynchronizeEvent e);

    Response visit(ConnectEvent connectEvent);
}
