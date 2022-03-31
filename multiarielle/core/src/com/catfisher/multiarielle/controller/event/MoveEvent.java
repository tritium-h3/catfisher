package com.catfisher.multiarielle.controller.event;

import com.catfisher.multiarielle.controller.EventVisitor;
import com.catfisher.multiarielle.controller.event.Event;
import com.catfisher.multiarielle.model.Character;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MoveEvent extends Event {
    Character character;
    int deltaX, deltaY;

    @Override
    public <Response> Response accept(EventVisitor<Response> v) {
        return v.visit(this);
    }
}
