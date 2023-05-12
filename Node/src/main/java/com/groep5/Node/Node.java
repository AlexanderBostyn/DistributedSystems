package com.groep5.Node;

import com.groep5.Node.Service.Discovery.DiscoveryService;
import com.groep5.Node.Service.Multicast.MulticastReceiver;
import com.groep5.Node.Service.Replication.Detection;
import com.groep5.Node.Service.Replication.StartUp;
import com.groep5.Node.Service.Replication.UpdateRemovedNode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.net.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
@Service
@SuppressWarnings("DataFlowIssue")
@Data
public class Node {
    private String nodeName;
    public int nodeHash;
    public int previousHash;
    public int nextHash;
    private final Inet4Address nodeAddress;
    public Inet4Address namingServerAddress;
    private final Logger logger = Logger.getLogger("Node");
    private int connectionsFinished = 0;
    public int numberOfNodes = -1;
    private Failure failure;
    public HashMap<File, ArrayList<String>> log = new HashMap<>();
    public File latestFile;
    private @Autowired
    DiscoveryService discoveryService;

    public Node() {
        try {
            this.nodeAddress = (Inet4Address) Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            logger.severe("Couldn't fetch own IP");
            throw new RuntimeException(e);
        }
    }

    public void startNode(String nodeName) {
        this.nodeName = nodeName;
        discoveryService.startDiscovery();
        bootstrap();
        new StartUp();
        new Detection().start();
    }

    //TODO kan ook in een service eigenlijk
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
        this.failure = new Failure(this);
        failure.start();
        listenToMulticasts();
    }


    public synchronized void sendUnicast(String message, InetSocketAddress address) throws IOException {
        logger.info("Sending Unicast to" + address + ", message: " + message);
        Socket socket = new Socket();
        socket.connect(address);
        PrintWriter printer = new PrintWriter(socket.getOutputStream(), true);
        printer.println(message);
        socket.close();
    }

    private void listenToMulticasts() {
        logger.info("");
        MulticastReceiver m = new MulticastReceiver();
        m.start();
    }


    public synchronized void finishConnection() {
        connectionsFinished++;
    }

    public void setNamingServerAddress(Inet4Address namingServerAddress) {
        this.namingServerAddress = namingServerAddress;
    }

    /**
     * Calculate the hash by requesting GET/hash/{name}.
     * @param nodeName the name of the node
     * @return the node generated from the namingserver
     */
    public int calculateHash(String nodeName) {
        logger.fine("Calculating hash: " + nodeName);
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
                .post()
                .uri("/node/" + nodeName)
                .bodyValue(this.nodeAddress.getHostAddress())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        logger.info(result);
    }


    //TODO not used
    public void setNodeHash(int nodeHash) {
        this.nodeHash = nodeHash;
    }

    public InetAddress getIp(int nodeHash) throws UnknownHostException {
        String result = WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                .get()
                .uri("/node/" + nodeHash)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return InetAddress.getByName(result);
    }

    public synchronized void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public synchronized void shutdownNode() throws IOException {

        //send id of next node to prev node
        //get address of node from namingserver
        logger.info("Shutting Down Node");
        failure.stop();
        if (nextHash != nodeHash) {
            InetAddress prevIp = getIp(previousHash);
            InetSocketAddress prevAddr = new InetSocketAddress(prevIp, 4321);
            sendUnicast(("shutdown;next;" + nextHash), prevAddr);
            //send id of prev node to next node
            InetAddress nextIp = getIp(nextHash);
            InetSocketAddress nextAddr = new InetSocketAddress(nextIp, 4321);
            sendUnicast(("shutdown;previous;" + previousHash), nextAddr);
            //new UpdateRemovedNode(this);
            new UpdateRemovedNode();
            logger.info("start updating nodes");
        }
        //move files to prev node unless prev node is local owner, then move to prev of prev node
        File directory = new File("src/main/resources");
        File[] files = directory.listFiles();
        //send files to prev node

        //remove node rom naming server
        InetSocketAddress namingAddr = new InetSocketAddress(namingServerAddress, 4321);
        Failure.deleteFromNamingServer(this, nodeHash);
        logger.info("test");
        System.out.println("node shutting down\n 0/ bye bye 0/");
        System.exit(0);
    }


    public synchronized Failure getFailure() {
        return failure;
    }

    public synchronized void setFailure(Failure failure) {
        this.failure = failure;
    }

    public void addLog(File f, String s) {
        ArrayList<String> list = log.get(f);
        list.add(s);
        log.replace(f, list);
    }
}
