package com.groep5.Node;

<<<<<<< HEAD
import com.groep5.Node.Multicast.MulticastReciever;
import com.groep5.Node.Multicast.MulticastSender;
import com.groep5.Node.Unicast.UnicastReceiver;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.*;
import java.util.HashMap;
import java.util.logging.Level;
=======
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
>>>>>>> discovery
import java.util.logging.Logger;

@SuppressWarnings("DataFlowIssue")
public class Node {
    private final String nodeName;
<<<<<<< HEAD
    private int nodeHash;
    private int previousHash;
    private int nextHash;
    private Inet4Address nodeAddress;
    private Inet4Address namingServerAddress;
    private Logger logger = Logger.getLogger("Node");
    private int connectionsFinished;
    private HashMap<Integer, InetAddress> nodeMap = new HashMap<>();
    public int numberOfNodes = 0;
=======
    public int nodeHash;
    public int previousHash;
    public int nextHash;
    private final Inet4Address nodeAddress;
    public Inet4Address namingServerAddress;
    private final Logger logger = Logger.getLogger("Node");
    private int connectionsFinished = 0;
    public int numberOfNodes = -1;
>>>>>>> discovery

    public Node(String nodeName) {
        this.nodeName = nodeName;
        try {
            this.nodeAddress = (Inet4Address) Inet4Address.getLocalHost();
<<<<<<< HEAD
            discovery();
            //this.namingServerAddress = (Inet4Address) Inet4Address.getLocalHost();
            listenToMulticast();
=======
>>>>>>> discovery
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        discovery();
        bootstrap();
    }


    private void discovery() {
        logger.info("started discovery");
        sendMulticast();
        listenToResponses();
        logger.info("Finished discovery");
    }

    private void bootstrap() {
        logger.info("started bootstrap");
        this.nodeHash = calculateHash(nodeName);
        logger.info("this NodeHash: " + this.nodeHash);

        // Check if the node is the only then set its parameters
        // in the other case the setting of parameters will already be handled by the unicasts received from other nodes.
        if (numberOfNodes == 0) {
            logger.info("Only node in network");
            this.previousHash = this.nodeHash;
            this.nextHash = this.nodeHash;
        }
        logger.info("Parameters set: ");
        logger.info("previousHash: " + previousHash);
        logger.info("nodeHash: " + nodeHash);
        logger.info("nextHash: " + nextHash);

        registerDevice();
        Failure f = new Failure(this);
        f.start();
        listenToMulticasts(f);
    }


    public synchronized void sendUnicast(String message, InetSocketAddress address) throws IOException {
        logger.info("Sending Unicast to" + address + ", message: " + message);
        Socket socket = new Socket();
        socket.connect(address);
        PrintWriter printer = new PrintWriter(socket.getOutputStream(), true);
        printer.println(message);
        socket.close();
    }

    private void listenToMulticasts(Failure f) {
        logger.info("");
        MulticastReceiver m = new MulticastReceiver(this, f);
        m.start();
    }

    private synchronized void sendMulticast() {
        String message = "discovery;" + this.nodeName + ";" + this.nodeAddress.getHostAddress();
        MulticastSender m = new MulticastSender(message);
        m.start();
    }

    private void listenToResponses() {
<<<<<<< HEAD
       UnicastReceiver unicastReceiver = new UnicastReceiver(this);
       unicastReceiver.run();
       if(!(numberOfNodes < 1 || (numberOfNodes > 0 && (numberOfNodes > connectionsFinished)))) {
           unicastReceiver.stop();
       }
=======
        UnicastReceiver unicastReceiver = new UnicastReceiver(this);
        unicastReceiver.start();
        while (numberOfNodes < 0 || (connectionsFinished < 3 && numberOfNodes > 0)) {
            logger.info("Number of nodes: " + numberOfNodes);
            logger.info("Finished connections: " + connectionsFinished);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //if the number of nodes is less than 0: naming server hasn't responded yet
            //in the other cases naming server has responded, if the network size is greater than 0 then it should receive 3 connections in total
            //if the network size is zero only the namingServer response is necessary.
//           logger.info("blocking");
        }
//        unicastReceiver.stopTask();

>>>>>>> discovery
    }

    public synchronized void finishConnection() {
        connectionsFinished++;
    }

    public void setNamingServerAddress(Inet4Address namingServerAddress) {
        this.namingServerAddress = namingServerAddress;
    }

    public int calculateHash(String nodeName) {
        return WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                .get()
                .uri("/hash/" + nodeName)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    /**
     * Register device with the nameServer using PUT/node/{nodeName}
     */
    private void registerDevice() {
        logger.info("registering device with the NamingServer");
        String result = WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                .put()
                .uri("/node/" + nodeName)
                .bodyValue(this.nodeAddress.getHostAddress())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        logger.info(result);
    }

<<<<<<< HEAD
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
=======
    public InetAddress getIp(int nodeHash) throws UnknownHostException {
        String result = WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                .get()
                .uri("/node/" + nodeHash)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return InetAddress.getByName(result);
>>>>>>> discovery
    }

    public synchronized void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

}
