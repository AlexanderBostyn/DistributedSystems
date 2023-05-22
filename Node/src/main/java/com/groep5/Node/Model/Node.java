package com.groep5.Node.Model;

import com.groep5.Node.Agents.SyncAgent;
import com.groep5.Node.Service.NodeLifeCycle.BootstrapService;
import com.groep5.Node.Service.NodeLifeCycle.DiscoveryService;
import com.groep5.Node.Service.Multicast.MulticastReceiver;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.NodeLifeCycle.Replication.ReplicationService;
import com.groep5.Node.Service.NodeLifeCycle.Replication.UpdateRemovedNode;
import com.groep5.Node.Service.Unicast.UnicastSender;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.*;

import java.io.IOException;
import java.util.logging.Logger;

@Component
@Data
public class Node {
    private  final Logger logger = Logger.getLogger(this.getClass().getName());

    private final DiscoveryService discoveryService;
    private  final NamingServerService namingServerService;
    private final BootstrapService bootstrapService;
    private final ReplicationService replicationService;
    private final NodePropreties nodePropreties;
    public final SyncAgent sAgent;
    @Autowired
    public Node(DiscoveryService discoveryService, NamingServerService namingServerService, BootstrapService bootstrapService, ReplicationService replicationService, NodePropreties nodePropreties) {
        this.nodePropreties = nodePropreties;

        this.discoveryService = discoveryService;
        this.namingServerService = namingServerService;

        this.sAgent = new SyncAgent(nodePropreties, namingServerService);

        this.bootstrapService = bootstrapService;
        this.replicationService = replicationService;
    }

    public void startNode(String nodeName) throws UnknownHostException {
        nodePropreties.setNodeName( nodeName);
        discoveryService.startDiscovery();
        bootstrapService.startBootstrap();
        namingServerService.registerDevice();
        nodePropreties.startNewFailure();

        listenToMulticasts();
        replicationService.startReplication();
    }



    private void listenToMulticasts() {
        logger.info("");
        MulticastReceiver m = new MulticastReceiver();
        m.start();
    }


    /**
     * Calculate the hash by requesting GET/hash/{name}.
     * @param nodeName the name of the node
     * @return the node generated from the namingserver
     */
    public  int calculateHash(String nodeName) {
        logger.fine("Calculating hash: " + nodeName);
        return namingServerService.calculateHash(nodeName);
    }

    /**
     * Register device with the nameServer using PUT/node/{nodeName}
     */

    public synchronized void setNumberOfNodes(int numberOfNodes) {
        nodePropreties.numberOfNodes = numberOfNodes;
    }

    public synchronized void shutdownNode() throws IOException {

        //send id of next node to prev node
        //get address of node from namingserver
        logger.info("Shutting Down Node");
        nodePropreties.stopFailure();
        if (nodePropreties.nextHash != nodePropreties.nodeHash) {
            Inet4Address prevIp = namingServerService.getIp(nodePropreties.previousHash);
            UnicastSender.sendMessage("shutdown;next;" + nodePropreties.nextHash, prevIp);

            //send id of prev node to next node
            Inet4Address nextIp = namingServerService.getIp(nodePropreties.nextHash);
            UnicastSender.sendMessage("shutdown;previous;" + nodePropreties.previousHash, nextIp);

            new UpdateRemovedNode();
            logger.info("start updating nodes");
        }

        //remove node rom naming server
        namingServerService.deleteNode(nodePropreties.nodeHash);
        logger.info("test");
        System.out.println("node shutting down\n 0/ bye bye 0/");
        System.exit(0);
    }


    /*public synchronized Failure getFailure() {
        return failure;
    }

    public synchronized void setFailure(Failure failure) {
        this.failure = failure;
    }

     */


}
