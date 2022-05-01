package com.catfisher.multiarielle;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.catfisher.multiarielle.MultiArielle;
import lombok.Getter;
import org.apache.commons.cli.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class  DesktopLauncher {
	public static void main (String[] args) throws Exception {
		CommandLine cl = new DefaultParser().parse(getOptions(), args);
		if (cl.hasOption("h")) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp("multiarielle", getOptions());
			return;
		}

		MultiArielle.ConnectionConfiguration defaultConfig = new MultiArielle.ConnectionConfiguration(
				cl.hasOption("u") ? cl.getOptionValue("u") : "",
				"",
				cl.hasOption("o") ? cl.getOptionValue("o") : "",
				cl.hasOption("p") ? Integer.parseInt(cl.getOptionValue("p")) : -1
		);

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(true);
		config.setWindowedMode(1280, 720);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new MultiArielle(defaultConfig), config);
	}

	private static Options getOptions() {
		Options options = new Options();
		options.addOption(
				Option.builder("p")
						.longOpt("port")
						.hasArg()
						.desc("Port to connect to server")
						.build()
		);
		options.addOption(
				Option.builder("o")
						.longOpt("host")
						.hasArg()
						.desc("Host running server")
						.build()
		);
		options.addOption(
				Option.builder("u")
						.longOpt("user")
						.hasArg()
						.desc("Username to connect with")
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
}
