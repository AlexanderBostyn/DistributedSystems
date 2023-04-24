package com.groep5.Node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NodeApplication.class, args);
		Node self = new Node("TestNode");
		self.start();
	}

}
