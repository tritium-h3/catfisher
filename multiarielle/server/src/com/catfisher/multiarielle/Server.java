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
        server.getTrueModel().loadBackground("bg.csv");
        int port = 8080;
        if (arg.length > 0) {
            port = Integer.parseInt(arg[0]);
        }

        ServerController serverController = new ServerController(server, port);

        log.info("Server started");

        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10000);
                    server.synchronizeAllClients();
                }
            } catch (InterruptedException e) {
                log.error("Interrupted while sleeping ", e);
            }
        }).start();

        serverController.run();
    }
}
