package com.groep5.Node.Service.NodeLifeCycle;

import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.Service.NamingServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class BootstrapService {
    private final NodePropreties nodePropreties;
    private final NamingServerService namingServerService;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    public BootstrapService(NodePropreties nodePropreties, NamingServerService namingServerService) {
        this.nodePropreties = nodePropreties;
        this.namingServerService=namingServerService;
    }

    public void startBootstrap() {
        logger.info("started bootstrap");
        nodePropreties.nodeHash = namingServerService.calculateHash(nodePropreties.getNodeName());
        logger.info("this NodeHash: " + nodePropreties.nodeHash);

        // Check if the node is the only then set its parameters
        // in the other case the setting of parameters will already be handled by the unicasts received from other nodes.
        if (nodePropreties.numberOfNodes == 0) {
            logger.info("Only node in network");
            nodePropreties.previousHash = nodePropreties.nodeHash;
            nodePropreties.nextHash = nodePropreties.nodeHash;
        }
        logger.info("Parameters set: ");
        logger.info("previousHash: " + nodePropreties.previousHash);
        logger.info("nodeHash: " + nodePropreties.nodeHash);
        logger.info("nextHash: " + nodePropreties.nextHash);

    }
}
