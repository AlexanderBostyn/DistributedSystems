package com.groep5.Node.Service.Replication;

import com.groep5.Node.Node;
import com.groep5.Node.SpringContext;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class UpdateRemovedNode {
    public Node node;
    public File[] files;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    /*public UpdateRemovedNode(Node node) throws UnknownHostException {
        this.node = node;
        lookForFiles();
        resendFiles();
    }*/
    public UpdateRemovedNode( ) throws UnknownHostException {
        this.node = getNode();
        lookForFiles();
        resendFiles();
    }
    private Node getNode() {
        return SpringContext.getBean(Node.class);
    }

    public void lookForFiles() {
        File directory = new File("src/main/resources");
        files = directory.listFiles();
        logger.info("Files: " + files);
    }

    public void resendFiles() throws UnknownHostException {
        for (File f : files) {
            logger.info("send file " + f.getName() + " to new location");
            // edge case: see if the previous node is the owner of the node
            logger.info("test file log: " + (node.log.get(f)).get(0));
            InetAddress ip = node.getIp(node.previousHash);
            if (node.log.get(f).get(0).equals(ip.toString())) {
                //file moet naar de previous node van de previous node gestuurd worden
            }
            else {
                //new SendFile(node, f, ip.toString()).start();
                new SendFile(f, ip.toString()).start();
                deleteFile(f);
            }
        }
    }

    public void deleteFile(File f) {
        if (f.delete()) {
            logger.info(f.getName() + " is deleted");
        }
    }

}
