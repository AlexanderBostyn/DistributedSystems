package com.groep5.Naming.server;

import com.groep5.Naming.server.Service.multicast.MulticastReciever;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.net.InetAddress;
import java.util.TreeMap;

@SpringBootApplication
public class NamingServerApplication {
	private static TreeMap<Integer, InetAddress> map;
	@Bean
	public TreeMap<Integer, InetAddress> mapping() {
		return map;
	}

	@Bean
	public File dataFile() {
		return new File("src/main/resources/data.json");
	}
	public static void main(String[] args) {


		ApplicationContext context = SpringApplication.run(NamingServerApplication.class, args);
		map = Persistence.LoadMap(context.getBean("dataFile", File.class));
		System.out.println(map);
		//start listening for multicasts
		new MulticastReciever().run();

	}

}
