package com.groep5.Node;

import com.groep5.Node.Multicast.MulticastReciever;
import com.groep5.Node.Multicast.MulticastSender;
import com.groep5.Node.Unicast.UnicastReceiver;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.*;
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
    private HashMap<Integer, InetAddress> nodeMap = new HashMap<>();
    public int numberOfNodes = 0;

    public Node(String nodeName) {
        this.nodeName = nodeName;
        try {
            this.nodeAddress = (Inet4Address) Inet4Address.getLocalHost();
            discovery();
            //this.namingServerAddress = (Inet4Address) Inet4Address.getLocalHost();
            listenToMulticast();
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
       if(!(numberOfNodes < 1 || (numberOfNodes > 0 && (numberOfNodes > connectionsFinished)))) {
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

    public void addNodeMap(int hash, InetAddress inetAddress) {
        this.nodeMap.put(hash, inetAddress);
    }

    public void listenToMulticast() {
        MulticastReciever multicastReciever = new MulticastReciever(this);
        multicastReciever.run();
    }

    public int getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(int previousHash) {
        this.previousHash = previousHash;
    }

    public int getNextHash() {
        return nextHash;
    }

    public void setNextHash(int nextHash) {
        this.nextHash = nextHash;
    }

    public int getNodeHash() {
        return nodeHash;
    }

    public void setNodeHash(int nodeHash) {
        this.nodeHash = nodeHash;
    }
}
