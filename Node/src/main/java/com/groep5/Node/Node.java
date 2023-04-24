package com.groep5.Node;

import org.apache.juli.logging.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    private int connectionsFinished;


    private HashMap<String, InetAddress> nodeMap = new HashMap<>();


    public int numberOfNodes = -1;

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
      return WebClient.create("http://"+ namingServerAddress.getHostAddress() + ":4321")
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
        String message = "discovery;" +this.nodeName + ";" + this.nodeAddress.getHostAddress();
        MulticastSender m = new MulticastSender(message);
        m.run();
    }

    private void listenToResponses() {
       UnicastReceiver unicastReceiver = new UnicastReceiver(this);
       unicastReceiver.run();
       while(numberOfNodes < 0 || (numberOfNodes > 0 && (numberOfNodes > connectionsFinished))) {
           unicastReceiver.stop();
       }

    }

    public void finishConnection() {
        connectionsFinished++;
    }

    public void setNamingServerAddress(Inet4Address namingServerAddress) {
        this.namingServerAddress = namingServerAddress;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public void addNodeMap(String nodeName, InetAddress inetAddress) {
        this.nodeMap.put(nodeName, inetAddress);
    }
}
