package com.catfisher.multiarielle.controller.event;

import com.catfisher.multiarielle.controller.EventVisitor;
import com.catfisher.multiarielle.controller.event.Event;
import com.catfisher.multiarielle.model.Character;
import lombok.Value;

@Value
public class MoveEvent extends Event {
    Character character;
    int deltaX, deltaY;

    @Override
    public <Response> Response accept(EventVisitor<Response> v) {
        return v.visit(this);
    }
}
