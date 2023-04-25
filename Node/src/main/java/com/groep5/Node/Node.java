package com.groep5.Node;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.logging.Logger;

@SuppressWarnings("DataFlowIssue")
public class Node {
    private final String nodeName;
    public int nodeHash;
    public int previousHash;
    public int nextHash;
    private final Inet4Address nodeAddress;
    public Inet4Address namingServerAddress;
    private final Logger logger = Logger.getLogger("Node");
    private int connectionsFinished = 0;
    public int numberOfNodes = -1;

    public Node(String nodeName) {
        this.nodeName = nodeName;
        try {
            this.nodeAddress = (Inet4Address) Inet4Address.getLocalHost();
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
        listenToMulticasts();
        new Failure(this).start();
    }


    public void sendUnicast(String message, InetSocketAddress address) throws IOException {
        Socket socket = new Socket();
        socket.connect(address);
        logger.info("Sending Unicast to" + socket.getInetAddress() + ", message: " + message);
        PrintWriter printer = new PrintWriter(socket.getOutputStream());
        printer.println(message);
        socket.close();
    }

    private void listenToMulticasts() {
        logger.info("");
        MulticastReceiver m = new MulticastReceiver(this);
        m.start();
    }

    private void sendMulticast() {
        String message = "discovery;" + this.nodeName + ";" + this.nodeAddress.getHostAddress();
        MulticastSender m = new MulticastSender(message);
        m.start();
    }

    private void listenToResponses() {
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
        unicastReceiver.stopTask();

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

    public InetAddress getIp(int nodeHash) throws UnknownHostException {
        String result = WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                .get()
                .uri("/file/" + nodeHash)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return InetAddress.getByName(result);
    }

    public synchronized void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

}
