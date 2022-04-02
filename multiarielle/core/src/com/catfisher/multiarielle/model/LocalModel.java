package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.clientServer.ModelClient;
import com.catfisher.multiarielle.controller.DeltaConsumer;
import com.catfisher.multiarielle.controller.delta.Delta;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LocalModel implements Model, DeltaConsumer<Boolean> {
    @Getter
    private final AbsoluteModel localModel = new AbsoluteModel();
    private ModelClient modelClient;

    public void associateClient(ModelClient client) {
        this.modelClient = client;
    }

    @Override
    public Boolean consume(Delta e) {
        log.info("localModel received event from keyboard {}", e);
        return localModel.consume(e) && modelClient.forwardDeltaToServer(e);
    }

    @Override
    public Iterable<SpritePlacement> getSpritePlacements(int startX, int startY, int endX, int endY) {
        return localModel.getSpritePlacements(startX, startY, endX, endY);
    }
}
