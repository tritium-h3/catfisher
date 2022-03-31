package com.catfisher.multiarielle.controller.event;

import com.catfisher.multiarielle.controller.EventVisitor;
import com.catfisher.multiarielle.model.AbsoluteModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collection;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SynchronizeEvent extends Event {
    Collection<AbsoluteModel.MutablePlacement> allCharacters;

    @Override
    public <Response> Response accept(EventVisitor<Response> v) {
        return v.visit(this);
    }
}
