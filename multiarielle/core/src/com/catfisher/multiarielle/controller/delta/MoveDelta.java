package com.catfisher.multiarielle.controller.delta;

import com.catfisher.multiarielle.controller.DeltaVisitor;
import com.catfisher.multiarielle.model.Character;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MoveDelta extends Delta {
    Character character;
    int deltaX, deltaY;

    @Override
    public <Response> Response accept(DeltaVisitor<Response> v) {
        return v.visit(this);
    }

    @Override
    public Delta invert() {
        return new MoveDelta(character, -deltaX, -deltaY);
    }
}
