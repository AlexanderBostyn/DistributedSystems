package com.groep5.Node.Model;

import com.groep5.Node.Service.NodeLifeCycle.BootstrapService;
import com.groep5.Node.Service.NodeLifeCycle.DiscoveryService;
import com.groep5.Node.Service.Multicast.MulticastReceiver;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.NodeLifeCycle.Failure;
import com.groep5.Node.Service.NodeLifeCycle.Replication.ReplicationService;
import com.groep5.Node.Service.NodeLifeCycle.Replication.UpdateRemovedNode;
import com.groep5.Node.Service.Unicast.UnicastSender;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@Component
@Data
public class Node {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final DiscoveryService discoveryService;
    private final NamingServerService namingServerService;
    private final BootstrapService bootstrapService;
    private final ReplicationService replicationService;
    private final NodePropreties nodePropreties;
    @Autowired
    public Node(DiscoveryService discoveryService, NamingServerService namingServerService, BootstrapService bootstrapService, ReplicationService replicationService, NodePropreties nodePropreties) {
        this.nodePropreties = nodePropreties;

        this.discoveryService = discoveryService;
        this.namingServerService = namingServerService;
        this.bootstrapService = bootstrapService;
        this.replicationService = replicationService;
    }

    public void startNode(String nodeName) {
        nodePropreties.setNodeName( nodeName);
        discoveryService.startDiscovery();
        bootstrapService.startBootstrap();
        namingServerService.registerDevice();

        nodePropreties.failure = new Failure(this);
        failure.start();
        listenToMulticasts();
        replicationService.startReplication();
    }

    //TODO kan ook in een service eigenlijk
    /*private void bootstrap() {
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
        namingServerService.registerDevice(this);

        this.failure = new Failure(this);
        failure.start();
        listenToMulticasts();
    }

     */

    private void listenToMulticasts() {
        logger.info("");
        MulticastReceiver m = new MulticastReceiver();
        m.start();
    }


    public synchronized void finishConnection() {
        connectionsFinished++;
    }


    /**
     * Calculate the hash by requesting GET/hash/{name}.
     * @param nodeName the name of the node
     * @return the node generated from the namingserver
     */
    public int calculateHash(String nodeName) {
        logger.fine("Calculating hash: " + nodeName);
        return namingServerService.calculateHash(nodeName);
    }

    /**
     * Register device with the nameServer using PUT/node/{nodeName}
     */

    public synchronized void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public synchronized void shutdownNode() throws IOException {

        //send id of next node to prev node
        //get address of node from namingserver
        logger.info("Shutting Down Node");
        failure.stop();
        if (nextHash != nodeHash) {
            Inet4Address prevIp = namingServerService.getIp(previousHash);
            UnicastSender.sendMessage("shutdown;next;" + nextHash, prevIp);

            //send id of prev node to next node
            Inet4Address nextIp = namingServerService.getIp(nextHash);
            UnicastSender.sendMessage("shutdown;previous;" + previousHash, nextIp);

            new UpdateRemovedNode();
            logger.info("start updating nodes");
        }

        //remove node rom naming server
        namingServerService.deleteNode(nodeHash);
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

    public void addLog(File f, Inet4Address ip) {
        ArrayList<Inet4Address> list = log.get(f);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(ip);
        log.put(f, list);
        logger.info("Current log: " + log.entrySet().toString());
    }

    public void dellLog(File f) {
        log.remove(f);
        logger.info("Current log: " + log.entrySet().toString());
    }
}
