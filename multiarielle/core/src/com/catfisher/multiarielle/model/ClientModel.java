package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;

import java.util.HashMap;
import java.util.Map;

public class ClientModel extends AbstractModel {
    private Map<Chunk.Address, Chunk> map = new HashMap<>();

    @Override
    public Map<Chunk.Address, Chunk> getMap() {
        return map;
    }

    public Boolean synchronize(SynchronizeEvent e) {
        synchronized (this) {
            map = e.getMap();
            allCharacters = e.getAllCharacters();
            return true;
        }
    }
}
