package com.catfisher.multiarielle.clientServer.event;

import com.catfisher.multiarielle.clientServer.event.client.ClientEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "direction")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ServerEvent.class, name = "ServerEvent"),
        @JsonSubTypes.Type(value = ClientEvent.class, name = "ClientEvent"),
})
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public abstract class Event {
}
