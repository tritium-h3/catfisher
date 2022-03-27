package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.controller.event.Event;

public interface EventConsumer<Response> {
    Response consume(Event e);
}
