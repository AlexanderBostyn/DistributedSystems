package com.groep5.Node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

@SpringBootApplication
public class NodeApplication {
	public NodeApplication(){
		node =  new Node();
	}
	private static Node node;

	@Bean
	public Node getNode() {
		return node;
	}

	public static void main(String[] args) {
		SpringApplication.run(NodeApplication.class, args);
		Logger.getAnonymousLogger().info(Arrays.toString(args));
		node.start(args[0]);
	}

	@PreDestroy
	public void preDestroy() throws IOException {
//		self.shutdown();
		System.out.println("shutting down!");
	}

}
