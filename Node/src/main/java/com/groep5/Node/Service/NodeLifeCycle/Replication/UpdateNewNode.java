package com.groep5.Node.Service.NodeLifeCycle.Replication;

import com.groep5.Node.Model.Log;
import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.Unicast.Senders.FileSender;
import com.groep5.Node.Service.Unicast.UnicastSender;
import com.groep5.Node.SpringContext;
import net.officefloor.plugin.variable.In;

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
    public ArrayList<File> replicatedFiles;
    public ArrayList<File> localFiles;
    public int receivedNodeHash;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Log log = NodeApplication.getLog();
    private final NamingServerService namingServerService;


    public UpdateNewNode(int recievedNodeHash) {
        this.namingServerService = NodeApplication.getNamingServerService();
        this.nodePropreties = NodeApplication.getNodePropreties();
        this.receivedNodeHash = recievedNodeHash;
        this.replicatedFiles = ReplicationService.listDirectory("src/main/resources/replicated");
        this.localFiles = ReplicationService.listDirectory("src/main/resources/local");
        try {
            resendLocalFiles();
            resendFiles();
//            if (nodePropreties.nextHash== recievedNodeHash){//might be new owner of our "owned" files
//                resendFiles();
//            }

        } catch (UnknownHostException e) {
            logger.severe("Error in retrieving ip from: " + recievedNodeHash);
            throw new RuntimeException(e);
        }
    }

    private int calcHash(File file) {
        return namingServerService.calculateHash(file.getName());
    }
    public void resendLocalFiles() throws UnknownHostException {
        Log sentLog = new Log();
        Inet4Address newIp = namingServerService.getIp(receivedNodeHash);
        for (File file : localFiles) {
            Inet4Address ip = ReplicationService.findIp(file.getName(), ReplicationState.STARTUP);
            FileSender sender_thread = UnicastSender.sendFile(file, ip, false,"replication");
            Log.LogEntry entry = log.get(file.getName());
            if (entry != null) {
                Log.LogEntry clonedEntry = entry.clone();
                sentLog.put(clonedEntry);
                log.delete(file.getName());
            }
            try {
                sender_thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (sentLog.size() > 0) {
            UnicastSender.sendLog(sentLog, newIp);
        }
    }

    private void resendFiles() throws UnknownHostException {
        Log sentLog = new Log();
        Inet4Address ip = namingServerService.getIp(receivedNodeHash);
        for (File file : replicatedFiles) {
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
                logger.info("file (" + file.getName() + ") is sent to node with hash:" + receivedNodeHash + "/" + ip.getHostAddress());
                Log.LogEntry entry = log.get(file.getName());
                FileSender fileSender = UnicastSender.sendFile(file, ip, true,"replication");
                if (entry != null) {
                    Log.LogEntry clonedEntry = entry.clone(); //copy our entry of that file

                    //only if we do not contain the file locally we do not add our own ip.
                    if (ReplicationService.listDirectory("src/main/resources/local").stream().noneMatch(localFile -> localFile.getName().equals(file.getName()))) {
                        logger.info("delete own address from log before sending "+ file.getName()+ "to "+ip.getHostAddress());
                        clonedEntry.delete(nodePropreties.getNodeAddress()); //delete our address from the entry because we will be removing it
                    }
                    sentLog.put(clonedEntry); //add the entry to the sentLog
                    log.delete(file.getName());
                }
                try {
                    fileSender.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ReplicationService.isOwner(file.getName(), nodePropreties.nodeHash) && log.get(file.getName()) != null &&log.get(file.getName()).size() < 2) {
                //if we are the owner of the file and the file is stored on only one location, we will send it to our new next.
                logger.info(file.getName()+" is only stored on 1 location, send to new node");
                UnicastSender.sendFile(file, ip, false,"replication");
                log.add(file.getName(), ip);
            }
        }
        if (sentLog.size() > 0) {
            UnicastSender.sendLog(sentLog, ip);
        }
    }
}

