package com.catfisher.multiarielle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.catfisher.multiarielle.clientServer.ModelClient;
import com.catfisher.multiarielle.clientServer.ProxyServer;
import com.catfisher.multiarielle.controller.UiController;
import com.catfisher.multiarielle.controller.delta.CharacterAddDelta;
import com.catfisher.multiarielle.controller.KeyboardController;
import com.catfisher.multiarielle.controller.delta.CharacterRemoveDelta;
import com.catfisher.multiarielle.model.Character;
import com.catfisher.multiarielle.model.LocalModel;
import com.catfisher.multiarielle.sprite.Sprite;
import com.catfisher.multiarielle.sprite.SpriteAtlas;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
public class MultiArielle extends Game {
	private final ConnectionConfiguration defaultConfiguration;

	@Value
	public static class ConnectionConfiguration {
		String username;
		String password;
		String address;
		int port;
	}
	
	EventLoopGroup workerGroup;
	@Getter
	private SpriteBatch batch;

	private ProxyServer server;
	private ModelClient client;

	@Getter
	private LocalModel localModel;

	@Getter
	private KeyboardController keyboardController;

	@Getter
	SpriteAtlas atlas;

	@Getter
	private Character hero;

	@Getter
	private BitmapFont charterBody;
	@Getter
	private BitmapFont charterHead;
	
	@Override
	public void create() {
		MainMenuScreen screen = new MainMenuScreen(this, "", defaultConfiguration);
		this.setScreen(screen);
	}
	
	public void runGame(ConnectionConfiguration config)  {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Charter Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter body = new FreeTypeFontGenerator.FreeTypeFontParameter();
		body.size = 15;
		charterBody = generator.generateFont(body);
		FreeTypeFontGenerator.FreeTypeFontParameter head = new FreeTypeFontGenerator.FreeTypeFontParameter();
		head.size = 15;
		charterHead = generator.generateFont(head);
		
		batch = new SpriteBatch();
		atlas = new SpriteAtlas();
		hero = new Character(Sprite.HERO, UUID.randomUUID().toString());

		localModel = new LocalModel();
		server = new ProxyServer();
		client = new ModelClient(config.getUsername(), localModel.getLocalModel());
		try {
			setupServerConn(server, config);
		}
		catch (ConnectException e) {
			MainMenuScreen screen = new MainMenuScreen(this, "Connection refused.", config);
			this.setScreen(screen);
			return;
		}
		localModel.associateClient(client);
		client.associateServer(server, config.getPassword());

		try {
			client.waitForServerReady();
 		} catch (InterruptedException exn) {
			log.info("Interrupted while waiting for server to be ready", exn);
			System.exit(0);
		}

		if (client.getDidServerReject().get()) {
			log.info("Server rejected.");
			MainMenuScreen screen = new MainMenuScreen(this, client.getError(), config);
			this.setScreen(screen);
			return;
		}

		localModel.consume(new CharacterAddDelta(hero, 10, 10));

		LocalScreen screen = new LocalScreen(this);
		client.setMessageHolder(screen.getMessageHolder());
		ChatHandler chatHandler = new ChatHandler(screen.getMessageHolder(), client);
		UiController uiController = new UiController(localModel, screen);
		keyboardController = new KeyboardController(hero, uiController, chatHandler);

		InputMultiplexer multiplexer = new InputMultiplexer(keyboardController, screen.getStage());

		Gdx.input.setInputProcessor(multiplexer);

		this.setScreen(screen);
	}

	public void setupServerConn(ProxyServer server, ConnectionConfiguration config) throws ConnectException {
		workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new LoggingHandler(LogLevel.INFO));
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
					ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
					ch.pipeline().addLast(server);
				}
			});

			// Start the client.
			ChannelFuture f = b.connect(config.getAddress(), config.getPort()).sync(); // (5)
			f.await();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void dispose () {
		batch.dispose();
		atlas.dispose();
		workerGroup.shutdownGracefully();
	}
}
