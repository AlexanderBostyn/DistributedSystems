package com.groep5.Node.Service.NodeLifeCycle;

import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.Service.Multicast.MulticastSender;
import com.groep5.Node.Service.Unicast.UnicastReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@SuppressWarnings("BusyWait")
@Service
public class DiscoveryService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private NodePropreties nodePropreties;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void startDiscovery()
    {
        logger.info("started discovery");
        sendMulticast();
        listenToResponses();
        logger.info("Finished discovery");
    }

    public  void sendMulticast() {
        String message = "discovery;" + nodePropreties.getNodeName() + ";" + nodePropreties.getNodeAddress().getHostAddress();
        MulticastSender m = new MulticastSender(message);
        m.start();
    }
    public void listenToResponses(){
        UnicastReceiver unicastReceiver = new UnicastReceiver();
        unicastReceiver.start();
        while (nodePropreties.getNumberOfNodes() < 0 || (nodePropreties.getConnectionsFinished() < 3 && nodePropreties.getNumberOfNodes() > 0)) {
            logger.info("Number of nodes: " + nodePropreties.getNumberOfNodes());
            logger.info("Finished connections: " + nodePropreties.getConnectionsFinished());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //if the number of nodes is less than 0: naming server hasn't responded yet
            //in the other cases naming server has responded, if the network size is greater than 0 then it should receive 3 connections in total
            //if the network size is zero only the namingServer response is necessary.
        }
    }

}
