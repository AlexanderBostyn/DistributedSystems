package com.groep5.Node.Service.NodeLifeCycle;

import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.Unicast.UnicastSender;
import com.groep5.Node.SpringContext;

import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

@SuppressWarnings("DataFlowIssue")
public class Failure extends Thread {
    public final NodePropreties nodePropreties;
    public final NamingServerService namingServerService;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Failure() {
        this.nodePropreties = getNodePropreties();
        this.namingServerService = getNamingServerService();
    }

    private NodePropreties getNodePropreties() {
        return SpringContext.getBean(NodePropreties.class);
    }
    private NamingServerService getNamingServerService() {
        return SpringContext.getBean(NamingServerService.class);
    }
    @Override
    public void run() {
        try {
            logger.info("Running failure check");
            while (!isInterrupted()) {
                Inet4Address nextNodeIp = (Inet4Address) getIp(nodePropreties.nextHash);
                Inet4Address previousNodeIp = (Inet4Address) getIp(nodePropreties.previousHash);
                if (nodePropreties.nextHash != nodePropreties.nodeHash) {
                    checkNodes(nextNodeIp, previousNodeIp);
                } else {
                    logger.fine("Only node in network: skipping failure detection");
                }
                //noinspection BusyWait
                Thread.sleep(5000);
            }
        } catch (UnknownHostException e) {
            logger.severe("Hostname not resolveable");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkNodes(Inet4Address nextNodeIp, Inet4Address previousNodeIp) {
        try {
            if (hasFailed(nextNodeIp)) {
                logger.severe("NextNode unreachable, starting recovery protocol");
                int newNextHash = namingServerService.getNextHash(nodePropreties.nextHash);
                namingServerService.deleteNode(nodePropreties.nextHash);
                Inet4Address newNextIp = getIp(newNextHash);
                UnicastSender.sendMessage("failure;previous;" + nodePropreties.nodeHash, newNextIp);

            } else {
                logger.fine("NextNode is still reachable");
            }
            if (hasFailed(previousNodeIp) && !hasFailed(nextNodeIp)) {
                logger.severe("PreviousNode unreachable, starting recovery protocol");
                int newPreviousHash = namingServerService.getPreviousHash(nodePropreties.previousHash);
                namingServerService.deleteNode(nodePropreties.previousHash);
                Inet4Address newPreviousIp = getIp(newPreviousHash);
                UnicastSender.sendMessage("failure;next;" + nodePropreties.nodeHash, newPreviousIp);
            } else {
                logger.fine("PreviousNode is still reachable");
            }
        } catch (UnknownHostException e) {
            logger.severe("Host Not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.severe("IoException while pinging the addresses");
            throw new RuntimeException(e);
        }
    }

    private synchronized boolean hasFailed(Inet4Address address) {
        try {
            logger.fine("pining: " + address.getHostAddress());
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, 4321));
            socket.close();
            logger.fine("received answer");
            return false;
        } catch (ConnectException e) {
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Inet4Address getIp(int nodeHash) throws UnknownHostException {
        return namingServerService.getIp(nodeHash);
    }
}
