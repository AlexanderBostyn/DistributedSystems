package com.groep5.Node;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node {
    private final String nodeName;
    private int nodeHash;
    private int previousHash;
    private int nextHash;
    private Inet4Address nodeAddress;
    private Inet4Address namingServerAddress;

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
        Logger.getLogger("Node").log(Level.SEVERE, nodeName + nodeHash);
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

    }

    private void listenToResponses() {

    }
}
