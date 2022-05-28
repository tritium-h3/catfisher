package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.clientServer.event.client.ClientChatEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientDeltaEvent;
import com.catfisher.multiarielle.clientServer.event.client.ConnectEvent;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.controller.delta.EntityChangeDelta;
import com.catfisher.multiarielle.model.AbstractModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StaticSprite.class, name = "StaticSprite"),
        @JsonSubTypes.Type(value = RandomWalkEntity.class, name = "RandomWalkEntity")
})
public interface Entity {
    String getId();
    EntityChangeDelta update(AbstractModel abstractModel);
}
