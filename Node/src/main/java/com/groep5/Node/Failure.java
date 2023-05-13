package com.groep5.Node;

import com.groep5.Node.Service.NamingServerService;

import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

@SuppressWarnings("DataFlowIssue")
public class Failure extends Thread {
    public final Node node;
    public final NamingServerService namingServerService;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Failure(Node node) {
        this.node = node;
        this.namingServerService = node.getNamingServerService();
    }

    @Override
    public void run() {
        try {
            logger.info("Running failure check");
            while (!isInterrupted()) {
                Inet4Address nextNodeIp = (Inet4Address) getIp(node.nextHash);
                Inet4Address previousNodeIp = (Inet4Address) getIp(node.previousHash);
                if (node.nextHash != node.nodeHash) {
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
                int newNextHash = namingServerService.getNextHash(node.nextHash);
                namingServerService.deleteNode(node.nextHash);
                InetAddress newNextIp = getIp(newNextHash);
                node.sendUnicast("failure;previous;" + node.nodeHash, new InetSocketAddress(newNextIp, 4321));

            } else {
                logger.fine("NextNode is still reachable");
            }
            if (hasFailed(previousNodeIp) && !hasFailed(nextNodeIp)) {
                logger.severe("PreviousNode unreachable, starting recovery protocol");
                int newPreviousHash = namingServerService.getPreviousHash(node.previousHash);
                namingServerService.deleteNode(node.previousHash);
                InetAddress newPreviousIp = getIp(newPreviousHash);
                node.sendUnicast("failure;next;" + node.nodeHash, new InetSocketAddress(newPreviousIp, 4321));
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
