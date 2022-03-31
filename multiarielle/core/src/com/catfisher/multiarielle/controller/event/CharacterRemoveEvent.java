package com.catfisher.multiarielle.controller.event;

import com.catfisher.multiarielle.controller.EventVisitor;
import com.catfisher.multiarielle.model.Character;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CharacterRemoveEvent extends Event {
    Character character;

    @Override
    public <Response> Response accept(EventVisitor<Response> v) {
        return v.visit(this);
    }
}
