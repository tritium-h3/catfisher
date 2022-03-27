package com.catfisher.multiarielle.controller.event;

import com.catfisher.multiarielle.controller.EventVisitor;

public abstract class Event {
    public abstract <Response> Response accept(EventVisitor<Response> v);
}
