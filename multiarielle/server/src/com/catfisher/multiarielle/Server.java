package com.catfisher.multiarielle;

import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.controller.ServerController;
import com.catfisher.multiarielle.worldgen.EmptyWorldGenerator;
import com.catfisher.multiarielle.worldgen.FileWorldGenerator;
import com.catfisher.multiarielle.worldgen.NoiseWorldGenerator;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.*;

import java.util.Scanner;

@Log4j2
public class Server {
    public static void main (String[] args) throws Exception {
        CommandLine cl = parseArgs(args);
        if (cl.hasOption("h") || !cl.hasOption("p")) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("multiarielle", getOptions());
            return;
        }
        String password = cl.hasOption("w") ? cl.getOptionValue("w") : "";
        ModelServer server = new ModelServer(password, new NoiseWorldGenerator());
        int port = Integer.parseInt(cl.getOptionValue("p"));
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

    private static Options getOptions() {
        Options options = new Options();
        options.addOption(
                Option.builder("p")
                        .longOpt("port")
                        .hasArg()
                        .desc("Port to listen on (required)")
                        .build()
        );
        options.addOption(
                Option.builder("w")
                        .longOpt("password")
                        .hasArg()
                        .desc("Password to connect to server")
                        .build()
        );
        options.addOption(
                Option.builder("h")
                        .longOpt("help")
                        .desc("Show this message")
                        .build()
        );
        return options;
    }

    private static CommandLine parseArgs(String[] args) throws ParseException {
        return new DefaultParser().parse(getOptions(), args);
    }

}
