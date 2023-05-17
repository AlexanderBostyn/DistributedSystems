package com.groep5.Node.Service.NodeLifeCycle.Replication;

import com.groep5.Node.Model.Log;
import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.Unicast.Senders.FileSender;
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
    private final Log log = NodeApplication.getLog();
    private final NamingServerService namingServerService;


    public UpdateNewNode(int recievedNodeHash) {
        this.namingServerService = NodeApplication.getNamingServerService();
        this.nodePropreties = NodeApplication.getNodePropreties();
        this.receivedNodeHash = recievedNodeHash;
        this.files = ReplicationService.listDirectory("src/main/resources/replicated");
        try {
            resendFiles();
        } catch (UnknownHostException e) {
            logger.severe("Error in retrieving ip from: " + recievedNodeHash);
            throw new RuntimeException(e);
        }
    }

    private int calcHash(File file) {
        return namingServerService.calculateHash(file.getName());
    }

    private void resendFiles() throws UnknownHostException {
        Log sentLog = new Log();
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
                logger.info("file (" + file.getName() + ") is send to node with hash:" + receivedNodeHash + "/" + ip.getHostAddress());
                FileSender fileSender = UnicastSender.sendFile(file, ip, true);
                Log.LogEntry entry = log.get(file.getName());
                if (entry != null) {
                    entry = entry.clone(); //copy our entry of that file
                    entry.delete(nodePropreties.getNodeAddress()); //delete our address from the entry because we will be removing it
                    sentLog.put(entry); //add the entry to the sentLog

                    // Wait till the file is done sending before deleting it.
                    deleteFile(file);
                }
            }
            if (ReplicationService.isOwner(file.getName(), nodePropreties.nodeHash) && log.get(file.getName()).size() < 2) {
                //if we are the owner of the file and the file is stored on only one location, we will send it to our new next.
                UnicastSender.sendFile(file, ip, false);
                log.add(file.getName(), ip);
            }
        }
        if (log.size() > 0) {
            UnicastSender.sendLog(sentLog, ip);
        }
    }


            private void deleteFile (File f){
                logger.info("result of fetching " + f.getName() + " from logs:" + log.get(f.getName()));
                logger.info("result of deleting " + f.getName() + " from logs: " + log.delete(f.getName()));
                if (f.delete()) {
                    logger.info(f.getName() + " is deleted from the replicas");
                } else {
                    logger.severe("Failed to remove file:" + f);
                }
            }
        }
