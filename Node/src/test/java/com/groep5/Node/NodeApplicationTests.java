package com.groep5.Node;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

class BroadcastSender {
	public static void main(String args[]) {
		try {
			DatagramSocket socket = new DatagramSocket();
			InetAddress address = InetAddress.getByName("255.255.255.255");
			socket.setBroadcast(true);
			byte[] buffer = "Hello".getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4455);
			socket.send(packet);
			socket.close();
		} catch (SocketException e) {
			throw new RuntimeException(e);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

class BroadcastReceiver {
	public static void main(String args[]) {
		try {
			DatagramSocket socket = new DatagramSocket(4455);
			byte[] buffer = new byte[255];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			System.out.println(Arrays.toString(packet.getData()));
		} catch (SocketException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}