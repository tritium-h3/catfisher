package com.catfisher.multiarielle.controller.delta;

import com.catfisher.multiarielle.clientServer.event.ConnectEvent;
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
        @JsonSubTypes.Type(value = MoveDelta.class, name = "MoveDelta"),
        @JsonSubTypes.Type(value = SynchronizeDelta.class, name = "SynchronizeDelta")
})
public abstract class Delta {
    public abstract <Response> Response accept(DeltaVisitor<Response> v);
}
