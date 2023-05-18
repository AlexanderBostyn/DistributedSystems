package com.groep5.Node.Service.NodeLifeCycle.Replication;

import com.groep5.Node.Model.Log;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.Unicast.UnicastSender;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class UpdateRemovedNode {
    public NodePropreties nodePropreties;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final NamingServerService namingServerService;

    public UpdateRemovedNode() throws UnknownHostException {
        this.nodePropreties = NodeApplication.getNodePropreties();
        this.namingServerService = NodeApplication.getNamingServer();
        resendFiles();
    }

    public void resendFiles() throws UnknownHostException {
        //Sent our entire log to our previous node.
        int previousHash = nodePropreties.previousHash;
        Inet4Address previousIp = namingServerService.getIp(previousHash);
        UnicastSender.sendLog(NodeApplication.getLog(), previousIp);
        logger.info("send entire log to " +  previousIp.getHostAddress());

        ArrayList<File> replicatedFiles = ReplicationService.listDirectory("src/main/resources/replicated");
        // al onze replicated files sturen we door naar onze vorige node.
        for (File file : replicatedFiles) {
            Inet4Address ip = ReplicationService.findIp(file.getName(), ReplicationState.SHUTDOWN);
            UnicastSender.sendFile(file, ip, true);
            logger.info("send file " + file.getName() + " to " + ip.getHostAddress());
        }

        ArrayList<File> localFiles = ReplicationService.listDirectory("src/main/resources/local");
        replicatedFiles.addAll(localFiles);
        //voor alle files moeten we vermelden dat we die niet meer bezitten.
        for (File file: replicatedFiles) {
            Inet4Address ownerIp = namingServerService.getFileOwner(namingServerService.calculateHash(file.getName()));
            if (ownerIp.equals(nodePropreties.getNodeAddress())) {
                //if we are the owner we should notify our previous becomes the new owner.
                ownerIp = namingServerService.getIp(nodePropreties.previousHash);
            }
            try {
                UnicastSender.sendMessage("shutdown;file;" + file.getName(), ownerIp);
            } catch (IOException e) {
                logger.severe("Error sending shutdown notice of files to: " + ownerIp.getHostAddress());
                throw new RuntimeException(e);
            }
        }
    }
}
