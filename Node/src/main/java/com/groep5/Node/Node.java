package com.groep5.Node;

import org.apache.juli.logging.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node {
    private final String nodeName;
    private int nodeHash;
    private int previousHash;
    private int nextHash;
    private Inet4Address nodeAddress;
    private Inet4Address namingServerAddress;
    private Logger logger = Logger.getLogger("Node");
    public Node(String nodeName) {
        this.nodeName = nodeName;
        try {
            this.nodeAddress = (Inet4Address) Inet4Address.getLocalHost();
            discovery();
            this.namingServerAddress = (Inet4Address) Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.nodeHash = calculateHash(this.nodeName);
        logger.log(Level.INFO, nodeName + "  " + nodeHash);
    }

    private int calculateHash(String nodeName) {
      return WebClient.create("http://"+ namingServerAddress.getHostAddress() + ":54321")
              .get()
              .uri("/hash/" + nodeName)
              .retrieve()
              .bodyToMono(Integer.class)
              .block();
    }

    private void discovery() {
        sendMulticast();
        listenToResponses();
    }

    private void sendMulticast() {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress group = InetAddress.getByName("230.0.0.0");
            String message = this.nodeName + ";" + this.nodeAddress.getHostAddress();
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 54322);
            socket.send(packet);
            logger.info("Multicast send: " + new String(packet.getData(), StandardCharsets.UTF_8));
            socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception when creating multicast");
            throw new RuntimeException(e);
        }
    }

    private void listenToResponses() {

    }
}
