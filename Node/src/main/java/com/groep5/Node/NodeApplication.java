package com.groep5.Node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

@SpringBootApplication
public class NodeApplication {
	private static Node self;

	public static void main(String[] args) {
		SpringApplication.run(NodeApplication.class, args);
		Logger.getAnonymousLogger().info(Arrays.toString(args));
		self = new Node(args[0]);
		self.start();
	}

	@PreDestroy
	public void preDestroy() throws IOException {
//		self.shutdown();
		System.out.println("shutting down!");
	}

}
