package com.catfisher.multiarielle.controller.delta;

import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.controller.DeltaVisitor;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CharacterAddDelta.class, name = "CharacterAddDelta"),
        @JsonSubTypes.Type(value = CharacterRemoveDelta.class, name = "CharacterRemoveDelta"),
        @JsonSubTypes.Type(value = CharacterMoveDelta.class, name = "CharacterMoveDelta"),
        @JsonSubTypes.Type(value = EntityChangeDelta.class, name = "EntityChangeDelta"),
        @JsonSubTypes.Type(value = SynchronizeEvent.class, name = "SynchronizeDelta")
})
public abstract class Delta {
    public abstract <Response> Response accept(DeltaVisitor<Response> v);

    public abstract Delta invert();
}
