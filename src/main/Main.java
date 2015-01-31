package main;

import ui.commandline.CLIMain;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		System.out.println(test());

        // Run Command Line Interface main()
        CLIMain.main(args);
	}

	public static String test() {
		return "Hello World!";
	}

}
