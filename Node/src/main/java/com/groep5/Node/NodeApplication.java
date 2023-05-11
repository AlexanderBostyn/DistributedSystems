package com.groep5.Node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

@SpringBootApplication
public class NodeApplication {
	//private Node thisNode;
	private static Node node;
	@Autowired

	public NodeApplication(Node thisNode){
		node =  thisNode;
	}

	/*@Bean
	public Node getNode() {
		return node;
	}

	 */

	public static void main(String[] args) {
		SpringApplication.run(NodeApplication.class, args);
		Logger.getAnonymousLogger().info(Arrays.toString(args));
		node.startNode(args[0]);
	}

	@PreDestroy
	public void preDestroy() throws IOException {
//		self.shutdown();
		System.out.println("shutting down!");
	}

}
