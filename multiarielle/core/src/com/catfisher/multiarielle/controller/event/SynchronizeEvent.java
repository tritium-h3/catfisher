package com.catfisher.multiarielle.controller.event;

import com.catfisher.multiarielle.controller.EventVisitor;
import com.catfisher.multiarielle.model.AbsoluteModel;
import lombok.Value;

import java.util.Collection;

@Value
public class SynchronizeEvent extends Event {
    Collection<AbsoluteModel.MutablePlacement> allCharacters;

    @Override
    public <Response> Response accept(EventVisitor<Response> v) {
        return v.visit(this);
    }
}
