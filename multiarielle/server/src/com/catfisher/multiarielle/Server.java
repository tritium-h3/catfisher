package com.catfisher.multiarielle;

import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.controller.ServerController;
import com.catfisher.multiarielle.worldgen.EmptyWorldGenerator;
import com.catfisher.multiarielle.worldgen.FileWorldGenerator;
import com.catfisher.multiarielle.worldgen.NoiseWorldGenerator;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;

@Log4j2
public class Server {
    public static void main (String[] arg) throws Exception {
        System.out.print("password> ");
        Scanner scanner = new Scanner(System.in);
        String pass = scanner.nextLine();
        log.info(pass);
        ModelServer server = new ModelServer(pass, new NoiseWorldGenerator());
        System.out.print("port> ");
        int port = Integer.parseInt(scanner.nextLine());
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
