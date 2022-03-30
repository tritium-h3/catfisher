package com.catfisher.multiarielle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.catfisher.multiarielle.clientServer.ModelClient;
import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.clientServer.ProxyServer;
import com.catfisher.multiarielle.controller.event.CharacterAddEvent;
import com.catfisher.multiarielle.controller.KeyboardController;
import com.catfisher.multiarielle.model.Character;
import com.catfisher.multiarielle.model.AbsoluteModel;
import com.catfisher.multiarielle.model.LocalModel;
import com.catfisher.multiarielle.sprite.Sprite;
import com.catfisher.multiarielle.sprite.SpriteAtlas;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Getter;

import java.util.UUID;


public class MultiArielle extends Game {
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

	@Override
	public void create () {
		batch = new SpriteBatch();
		atlas = new SpriteAtlas();
		Character hero = new Character(Sprite.HERO, UUID.randomUUID().toString());

		localModel = new LocalModel();
		server = new ProxyServer();
		client = new ModelClient(localModel.getLocalModel());
		setupServerConn(server);
		localModel.associateClient(client);
		client.associateServer(server);

		localModel.consume(new CharacterAddEvent(hero, 10, 10));
		keyboardController = new KeyboardController(hero, localModel);

		Gdx.input.setInputProcessor(keyboardController);

		this.setScreen(new LocalScreen(this));
	}

	public void setupServerConn(ProxyServer server) {
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
					ch.pipeline().addLast(server);
				}
			});

			// Start the client.
			ChannelFuture f = b.connect("localhost", 8080).sync(); // (5)

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
