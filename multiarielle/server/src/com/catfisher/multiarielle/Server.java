package com.catfisher.multiarielle;

import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.controller.AiController;
import com.catfisher.multiarielle.controller.ServerController;
import com.catfisher.multiarielle.coordinates.TileCoordinate;
import com.catfisher.multiarielle.entity.SearchingEntity;
import com.catfisher.multiarielle.entity.SpriteEntity;
import com.catfisher.multiarielle.model.Chunk;
import com.catfisher.multiarielle.sprite.Sprite;
import com.catfisher.multiarielle.worldgen.NoiseWorldGenerator;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.*;

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

        Thread serverThread = new Thread(serverController);
        serverThread.start();

        AiController aiController = new AiController(server);
        new Thread(aiController).start();

        SpriteEntity testEntity = new SearchingEntity(5, 5, Sprite.VILLAGER);
        server.getTrueModel().getMap().get(Chunk.Address.ofTileCoords(new TileCoordinate(testEntity.getX(), testEntity.getY()))).getEntities().put(testEntity.getId(), testEntity);
        aiController.registerWaiter(testEntity, 1000);

        serverThread.join();
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
