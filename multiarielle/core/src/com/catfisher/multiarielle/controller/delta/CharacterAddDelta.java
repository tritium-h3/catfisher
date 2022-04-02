package com.catfisher.multiarielle.controller.delta;

import com.catfisher.multiarielle.controller.DeltaVisitor;
import com.catfisher.multiarielle.model.Character;
import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CharacterAddDelta extends Delta {
    Character character;
    int x, y;

    @Override
    public <Response> Response accept(DeltaVisitor<Response> v) {
        return v.visit(this);
    }
}
