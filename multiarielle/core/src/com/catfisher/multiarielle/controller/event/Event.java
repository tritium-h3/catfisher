package com.catfisher.multiarielle.controller.event;

import com.catfisher.multiarielle.controller.EventVisitor;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CharacterAddEvent.class, name = "CharacterAddEvent"),
        @JsonSubTypes.Type(value = MoveEvent.class, name = "MoveEvent"),
        @JsonSubTypes.Type(value = SynchronizeEvent.class, name = "SynchronizeEvent")

})
public abstract class Event {
    public abstract <Response> Response accept(EventVisitor<Response> v);
}
