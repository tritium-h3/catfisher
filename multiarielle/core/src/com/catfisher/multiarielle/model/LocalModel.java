package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.clientServer.ModelClient;
import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.controller.EventConsumer;
import com.catfisher.multiarielle.controller.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LocalModel implements Model, EventConsumer<Boolean> {
    @Getter
    private final AbsoluteModel localModel = new AbsoluteModel();
    private ModelClient modelClient;

    public void associateClient(ModelClient client) {
        this.modelClient = client;
    }

    @Override
    public Boolean consume(Event e) {
        log.info("localModel received event from keyboard {}", e);
        return localModel.consume(e) && modelClient.forwardEventToServer(e);
    }

    @Override
    public Iterable<SpritePlacement> getSpritePlacements(int startX, int startY, int endX, int endY) {
        return localModel.getSpritePlacements(startX, startY, endX, endY);
    }
}
