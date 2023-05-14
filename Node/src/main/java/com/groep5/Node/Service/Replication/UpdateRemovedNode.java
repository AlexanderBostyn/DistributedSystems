package com.groep5.Node.Service.Replication;

import com.groep5.Node.Node;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.Unicast.UnicastSender;
import com.groep5.Node.SpringContext;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Logger;

public class UpdateRemovedNode {
    public Node node;
    public File[] files;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final NamingServerService namingServerService;

    public UpdateRemovedNode( ) throws UnknownHostException {
        this.node = getNode();
        this.namingServerService = node.getNamingServerService();
        lookForFiles();
        resendFiles();
    }
    private Node getNode() {
        return SpringContext.getBean(Node.class);
    }

    public void lookForFiles() {
        File directory = new File("src/main/resources");
        files = directory.listFiles();
        logger.info("Files: " + Arrays.toString(files));
    }

    //TODO functie afwerken zie specs bovenaan Replication
    public void resendFiles() throws UnknownHostException {
        for (File f : files) {
            logger.info("send file " + f.getName() + " to new location");
            // edge case: see if the previous node is the owner of the node
            logger.info("test file log: " + (node.log.get(f)).get(0));
            Inet4Address ip = namingServerService.getIp(node.previousHash);
            if (node.log.get(f).get(0).equals(ip.toString())) {
                //file moet naar de previous node van de previous node gestuurd worden
                //TODO
            }
            else {

                UnicastSender.sendFile(f, ip);
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
