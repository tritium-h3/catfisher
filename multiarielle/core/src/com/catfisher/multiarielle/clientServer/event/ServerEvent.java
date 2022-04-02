package com.catfisher.multiarielle.clientServer.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ServerDeltaEvent.class, name = "ServerDeltaEvent")
})
@NoArgsConstructor
public abstract class ServerEvent extends Event {
    public abstract <Response> Response receive(ServerEventVisitor<Response> visitor);
}
