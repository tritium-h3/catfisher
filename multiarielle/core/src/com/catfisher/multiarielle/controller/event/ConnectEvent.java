package com.catfisher.multiarielle.controller.event;

import com.catfisher.multiarielle.controller.EventVisitor;

public class ConnectEvent extends Event {

    @Override
    public <Response> Response accept(EventVisitor<Response> v) {
        return v.visit(this);
    }
}
