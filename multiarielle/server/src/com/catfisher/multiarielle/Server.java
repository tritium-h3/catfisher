package com.catfisher.multiarielle;

import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.controller.ServerController;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Server {
    public static void main (String[] arg) throws Exception {
        ModelServer server = new ModelServer();
        int port = 8080;
        if (arg.length > 0) {
            port = Integer.parseInt(arg[0]);
        }

        ServerController serverController = new ServerController(server, port);

        log.info("Server started");

        serverController.run();
    }
}
