package com.groep5.Node.Service.NodeLifeCycle.Replication;

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
        ArrayList<File> replicatedFiles = ReplicationService.listDirectory("src/main/resources/replicated");
        for (File file : replicatedFiles) {
            Inet4Address ip = ReplicationService.findIp(file.getName(), ReplicationState.SHUTDOWN);
            UnicastSender.sendFile(file, ip);
            logger.info("send file " + file.getName() + " to " + ip.getHostAddress());
        }
        int previousHash = nodePropreties.previousHash;
        Inet4Address previousIp = namingServerService.getIp(previousHash);
        UnicastSender.sendLog(nodePropreties.getLog(), previousIp);
        logger.info("send entire log to " +  previousIp.getHostAddress());

        ArrayList<File> localFiles = ReplicationService.listDirectory("src/main/resources/local");
        for (File file: localFiles) {
            Inet4Address ownerIp = namingServerService.getFileOwner(namingServerService.calculateHash(file.getName()));
            try {
                UnicastSender.sendMessage("shutdown;file;" + file.getName(), ownerIp);
            } catch (IOException e) {
                logger.severe("Error sending shutdown notice of files to: " + ownerIp.getHostAddress());
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteFile(File f) {
        if (f.delete()) {
            logger.info(f.getName() + " is deleted");
        }
    }
}
