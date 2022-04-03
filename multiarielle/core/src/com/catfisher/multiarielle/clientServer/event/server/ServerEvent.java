package com.catfisher.multiarielle.clientServer.event.server;

import com.catfisher.multiarielle.clientServer.event.Event;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NoArgsConstructor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ServerDeltaEvent.class, name = "ServerDeltaEvent"),
        @JsonSubTypes.Type(value = SynchronizeEvent.class, name = "SynchronizeEvent")
})
@NoArgsConstructor
public abstract class ServerEvent extends Event {
    public abstract <Response> Response receive(ServerEventVisitor<Response> visitor);
}
