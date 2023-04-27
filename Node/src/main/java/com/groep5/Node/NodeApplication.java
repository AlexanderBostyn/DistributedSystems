package com.groep5.Node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.logging.Logger;

@SpringBootApplication
public class NodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NodeApplication.class, args);
		Logger.getAnonymousLogger().info(Arrays.toString(args));
		Node self = new Node(args[0]);
		self.start();
	}

}
