package com.groep5.Node;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

@SuppressWarnings("DataFlowIssue")
public class Failure extends Thread {
    public final Node node;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Failure(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        try {
            logger.info("Running failure check");
            while (!isInterrupted()) {
                Inet4Address nextNodeIp = (Inet4Address) node.getIp(node.nextHash);
                Inet4Address previousNodeIp = (Inet4Address) node.getIp(node.previousHash);
                if (node.nextHash != node.nodeHash) {
                    checkNodes(nextNodeIp, previousNodeIp);
                } else {
                    logger.info("Only node in network: skipping failure detection");
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
                int newNextHash = WebClient.create("http://" + node.namingServerAddress.getHostAddress() + ":54321")
                        .get()
                        .uri("/node/" + node.nextHash + "/next")
                        .retrieve()
                        .bodyToMono(Integer.class)
                        .block();
                deleteFromNamingServer(node, node.nextHash);
                InetAddress newNextIp = node.getIp(newNextHash);
                node.sendUnicast("failure;previous;" + node.nodeHash, new InetSocketAddress(newNextIp, 4321));

            } else {
                logger.info("NextNode is still reachable");
            }
            if (hasFailed(previousNodeIp) && !hasFailed(nextNodeIp)) {
                logger.severe("PreviousNode unreachable, starting recovery protocol");
                int newPreviousHash = WebClient.create("http://" + node.namingServerAddress.getHostAddress() + ":54321")
                        .get()
                        .uri("/node/" + node.previousHash + "/previous")
                        .retrieve()
                        .bodyToMono(Integer.class)
                        .block();
                deleteFromNamingServer(node, node.previousHash);
                InetAddress newPreviousIp = node.getIp(newPreviousHash);
                node.sendUnicast("failure;next;" + node.nodeHash, new InetSocketAddress(newPreviousIp, 4321));
            } else {
                logger.info("PreviousNode is still reachable");
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
            logger.info("pining: " + address.getHostAddress());
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, 4321));
            socket.close();
            logger.info("received answer");
            return false;
        } catch (ConnectException e) {
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static synchronized void deleteFromNamingServer(Node node, int hash) {
        WebClient.create("http://" + node.namingServerAddress.getHostAddress() + ":54321")
                .delete()
                .uri("/node/" + hash)
                .retrieve()
                .bodyToMono(String.class).block();
        Logger.getAnonymousLogger().info("Removed the failed from the namingserver");
    }
}
