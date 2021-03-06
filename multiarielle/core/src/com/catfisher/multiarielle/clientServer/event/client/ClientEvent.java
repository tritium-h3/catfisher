package com.catfisher.multiarielle.clientServer.event.client;

import com.catfisher.multiarielle.clientServer.event.Event;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConnectEvent.class, name = "ConnectEvent"),
        @JsonSubTypes.Type(value = ClientDeltaEvent.class, name = "ClientDeltaEvent"),
        @JsonSubTypes.Type(value = ClientChatEvent.class, name = "ClientChatEvent"),
})
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public abstract class ClientEvent extends Event {
    @Getter @Setter
    String clientId;

    public abstract <Response> Response receive(ClientEventVisitor<Response> visitor);
}
