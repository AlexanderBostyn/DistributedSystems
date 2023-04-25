package com.groep5.Node;

import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

@SuppressWarnings("DataFlowIssue")
public class Failure extends Thread{
    private final Node node;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Failure(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        checkNodes();
    }
    private void checkNodes() {
        logger.info("Checking nodes");
        try {
            Inet4Address nextNodeIp = (Inet4Address) node.getIp(node.nextHash);
            Inet4Address previousNodeIp = (Inet4Address) node.getIp(node.previousHash);
            do {
                if (!nextNodeIp.isReachable(1000)) {
                    logger.severe("NextNode unreachable, starting recovery protocol");
                    int newNextHash = WebClient.create("http://" + node.namingServerAddress.getHostAddress() + ":54321")
                            .get()
                            .uri("/node/" + node.nextHash + "/next")
                            .retrieve()
                            .bodyToMono(Integer.class)
                            .block();
                    InetAddress newNextIp = node.getIp(newNextHash);
                    node.sendUnicast("failure;previous;" + node.nodeHash, new InetSocketAddress(newNextIp, 4321));
                    //We put the responsibility to remove the failed node with the previous node.
                    WebClient.create("http://" + node.namingServerAddress.getHostAddress() + ":54321")
                            .delete()
                            .uri("/node/" + node.nextHash)
                            .retrieve();
                    logger.info("Removed the failed from the namingserver");
                }
                logger.fine("NextNode is still reachable");
                if (!previousNodeIp.isReachable(1000)) {
                    logger.severe("PreviousNode unreachable, starting recovery protocol");
                    int newPreviousHash = WebClient.create("http://" + node.namingServerAddress.getHostAddress() + ":54321")
                            .get()
                            .uri("/node/" + node.previousHash + "/previous")
                            .retrieve()
                            .bodyToMono(Integer.class)
                            .block();
                    InetAddress newPreviousIp = node.getIp(newPreviousHash);
                    node.sendUnicast("failure;next;" + node.nodeHash, new InetSocketAddress(newPreviousIp, 4321));
                }
                logger.fine("PreviousNode is still reachable");
            } while (true);
        } catch (UnknownHostException e) {
            logger.severe("Host Not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.severe("IoException while pinging the addresses");
            throw new RuntimeException(e);
        }
    }
}
