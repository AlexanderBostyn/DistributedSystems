package com.groep5.Node.Service.NodeLifeCycle;

import com.groep5.Node.Model.Node;
import com.groep5.Node.Service.NamingServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class BootstrapService {
    private final Node node;
    private final NamingServerService namingServerService;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    public BootstrapService(Node node, NamingServerService namingServerService) {
        this.node = node;
        this.namingServerService=namingServerService;
    }

    public void startBootstrap() {
        logger.info("started bootstrap");
        node.nodeHash = node.calculateHash(node.getNodeName());
        logger.info("this NodeHash: " + node.nodeHash);

        // Check if the node is the only then set its parameters
        // in the other case the setting of parameters will already be handled by the unicasts received from other nodes.
        if (node.numberOfNodes == 0) {
            logger.info("Only node in network");
            node.previousHash = node.nodeHash;
            node.nextHash = node.nodeHash;
        }
        logger.info("Parameters set: ");
        logger.info("previousHash: " + node.previousHash);
        logger.info("nodeHash: " + node.nodeHash);
        logger.info("nextHash: " + node.nextHash);

    }
}
