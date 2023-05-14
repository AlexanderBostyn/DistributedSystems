package com.groep5.Node.Service.Replication;

import com.groep5.Node.Node;
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
    public Node node;
    public ArrayList<File> files;
    public int receivedNodeHash;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public UpdateNewNode(int recievedNodeHash) {
        this.node = getNode();
        this.receivedNodeHash = recievedNodeHash;
        this.files = Replication.listDirectory("src/main/resources/replicated");
        try {
            resendFiles();
        } catch (UnknownHostException e) {
            logger.severe("Error in retrieving ip from: " + recievedNodeHash);
            throw new RuntimeException(e);
        }
    }

    private int calcHash(File file) {
        return node.calculateHash(file.getName());
    }

    private void resendFiles() throws UnknownHostException {

        HashMap<File, ArrayList<Inet4Address>> log = new HashMap<>();
        Inet4Address ip = node.getNamingServerService().getIp(receivedNodeHash);
        for (File file : files) {
            int fileHash = calcHash(file);
            int newNextNodeHash = receivedNodeHash;
            // The new next NodeHash is smaller than our hash, this means we are at the end of our ring.
            // This creates problems for linearity that can be fixed if we just add 32768 to all the hashes that are at the beginning of the ring
            if (newNextNodeHash < node.nodeHash) {
                newNextNodeHash += 32768;

                // The file is also located at the beginning of the ring, adding 32768 will make the ring linear again.
                if (fileHash < node.nodeHash) {
                    fileHash += 32768;
                }
            }
            if (fileHash > newNextNodeHash) {
                logger.info("file (" + file.getName() + ") is send to node with hash:" + receivedNodeHash);
                UnicastSender.sendFile(file, ip);
                ArrayList<Inet4Address> entry = (ArrayList<Inet4Address>) node.log.get(file).clone();
                log.put(file, entry);
                deleteFile(file);
            }
        }
        UnicastSender.sendLog(log, ip);
    }

    private void deleteFile(File f) {
        if (f.delete()) {
            node.dellLog(f);
            logger.info(f.getName() + " is deleted");
        }
    }

    private Node getNode() {
        return SpringContext.getBean(Node.class);
    }
}
