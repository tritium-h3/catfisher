package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.controller.EventConsumer;
import com.catfisher.multiarielle.controller.event.Event;
import com.catfisher.multiarielle.model.AbsoluteModel;
import com.catfisher.multiarielle.model.Model;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
public class ModelClient implements EventConsumer<Boolean> {
    private final AbsoluteModel localCopy;
    private ModelServer server;
    @Getter
    private final String clientId;

    public ModelClient(AbsoluteModel localCopy) {
        this.localCopy = localCopy;
        this.clientId = UUID.randomUUID().toString();
    }

    public void associateServer(ModelServer server) {
        this.server = server;
        // server.addClient(this);
        // TODO: Server networking
    }

    @Override
    public Boolean consume(Event e) {
        log.info("Client received from server event {}", e);
        return localCopy.consume(e);
    }

    public boolean forwardEventToServer(Event e) {
        return server.receive(new ModelServer.ClientEvent(clientId, e));
    }
}
