package com.catfisher.multiarielle;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.catfisher.multiarielle.MultiArielle;
import lombok.Getter;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class  DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(true);
		config.setWindowedMode(1280, 720);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new MultiArielle(), config);
	}
}
