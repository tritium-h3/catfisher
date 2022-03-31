package com.catfisher.multiarielle.controller.event;

import com.catfisher.multiarielle.controller.EventVisitor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor
public class ConnectEvent extends Event {

    @Override
    public <Response> Response accept(EventVisitor<Response> v) {
        return v.visit(this);
    }
}
