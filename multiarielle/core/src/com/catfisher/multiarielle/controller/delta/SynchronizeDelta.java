package com.catfisher.multiarielle.controller.delta;

import com.catfisher.multiarielle.controller.DeltaVisitor;
import com.catfisher.multiarielle.model.AbsoluteModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collection;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SynchronizeDelta extends Delta {
    Collection<AbsoluteModel.MutablePlacement> allCharacters;

    @Override
    public <Response> Response accept(DeltaVisitor<Response> v) {
        return v.visit(this);
    }
}
