package com.groep5.Node.Service.NodeLifeCycle.Replication;

import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.Unicast.UnicastSender;
import com.groep5.Node.SpringContext;

import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;


/**
 * The method class should be called when our nextNode is changed
 * Then it should resend the files with hashes greater than the new nextNode.
 */
public class UpdateNewNode {
    public NodePropreties nodePropreties;
    public ArrayList<File> files;
    public int receivedNodeHash;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ReplicationService replicationService;
    private final NamingServerService namingServerService;


    public UpdateNewNode(int recievedNodeHash) {
        this.namingServerService = getNamingServerService();
        this.nodePropreties = getNodePropreties();
        this.receivedNodeHash = recievedNodeHash;
        this.replicationService = getReplicationService();
        this.files = replicationService.listDirectory("src/main/resources/replicated");
        try {
            resendFiles();
        } catch (UnknownHostException e) {
            logger.severe("Error in retrieving ip from: " + recievedNodeHash);
            throw new RuntimeException(e);
        }
    }
    private ReplicationService getReplicationService() {
        return SpringContext.getBean(ReplicationService.class);
    }

    private int calcHash(File file) {
        return namingServerService.calculateHash(file.getName());
    }

    private void resendFiles() throws UnknownHostException {

        HashMap<File, ArrayList<Inet4Address>> log = new HashMap<>();
        Inet4Address ip = namingServerService.getIp(receivedNodeHash);
        for (File file : files) {
            int fileHash = calcHash(file);
            int newNextNodeHash = receivedNodeHash;
            // The new next NodeHash is smaller than our hash, this means we are at the end of our ring.
            // This creates problems for linearity that can be fixed if we just add 32768 to all the hashes that are at the beginning of the ring
            if (newNextNodeHash < nodePropreties.nodeHash) {
                newNextNodeHash += 32768;

                // The file is also located at the beginning of the ring, adding 32768 will make the ring linear again.
                if (fileHash < nodePropreties.nodeHash) {
                    fileHash += 32768;
                }
            }
            if (fileHash > newNextNodeHash) {
                logger.info("file (" + file.getName() + ") is send to node with hash:" + receivedNodeHash);
                UnicastSender.sendFile(file, ip);
                ArrayList<Inet4Address> entry =  nodePropreties.log.get(file);
                if (entry != null) {
                    entry = (ArrayList<Inet4Address>) entry.clone();
                    log.put(file, entry);
                    deleteFile(file);
                }
            }
        }
        if (log.size() > 0) {
            UnicastSender.sendLog(log, ip);
        }
    }

    private void deleteFile(File f) {
        if (f.delete()) {
            nodePropreties.dellLog(f);
            logger.info(f.getName() + " is deleted from the replicas");
        }
    }

    private NodePropreties getNodePropreties() {
        return SpringContext.getBean(NodePropreties.class);
    }
    private NamingServerService getNamingServerService() {
        return SpringContext.getBean(NamingServerService.class);
    }
}
