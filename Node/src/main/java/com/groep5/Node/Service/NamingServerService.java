package com.groep5.Node.Service;

import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.logging.Logger;




/**
 * This class defines the helper functions for making rest calls to our namingServer;
 */
@Service
public class NamingServerService {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Inet4Address namingServerAddress;
    private final NodePropreties nodePropreties;
    @Autowired
    public NamingServerService(NodePropreties nodePropreties) {
        this.nodePropreties = nodePropreties;
    }


    /**
     * Asks namingServer to calculate the hash of a string
     *
     * @param name the string that needs to be hashed
     * @return integer hash
     */
    @SuppressWarnings("DataFlowIssue")
    public int calculateHash(String name) {
        if (namingServerAddress != null) {
            return WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                    .get()
                    .uri("/hash/" + name)
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();
        }
        logger.severe("NamingServer Address not yet set");
        throw new RuntimeException("Tried to contact namingServer but ip was not yet resolved");
    }

    /**
     * Get the next hash from a given node's hash.
     *
     * @param nodeHash The hash of the node you want its neighbour from.
     * @return The Hash of the Node's next neighbour
     */
    @SuppressWarnings("DataFlowIssue")
    public int getNextHash(int nodeHash) {
        if (namingServerAddress != null) {
            return WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                    .get()
                    .uri("/node/" + nodeHash + "/next")
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();
        }
        logger.severe("NamingServer Address not yet set");
        throw new RuntimeException("Tried to contact namingServer but ip was not yet resolved");
    }

    /**
     * Get the previous hash from a given node's hash.
     *
     * @param nodeHash The hash of the node you want its neighbour from.
     * @return The Hash of the Node's previous neighbour
     */
    @SuppressWarnings("DataFlowIssue")
    public int getPreviousHash(int nodeHash) {
        if (namingServerAddress != null) {
            return WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                    .get()
                    .uri("/node/" + nodeHash + "/previous")
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();
        }
        logger.severe("NamingServer Address not yet set");
        throw new RuntimeException("Tried to contact namingServer but ip was not yet resolved");
    }

    /**
     * Remove a node from the namingServer given its hash.
     *
     * @param nodeHash The hash of the node you want to remove.
     */
    public void deleteNode(int nodeHash) {
        if (namingServerAddress != null) {
            WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                    .delete()
                    .uri("/node/" + nodeHash)
                    .retrieve()
                    .bodyToMono(String.class).block();
            logger.info("Removed the failed node from the namingServer: " + nodeHash);
            return;
        }
        logger.severe("NamingServer Address not yet set");
        throw new RuntimeException("Tried to contact namingServer but ip was not yet resolved");
    }

    /**
     * Register the node with the naming server.
     * @param node the Node singleton.
     */
    public void registerDevice() {
        if (namingServerAddress != null) {
            WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                    .post()
                    .uri("/node/" + nodePropreties.getNodeName())
                    .bodyValue(nodePropreties.getNodeAddress().getHostAddress())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            logger.info("Successfully registered with naming server");
            return;
        }
        logger.severe("NamingServer Address not yet set");
        throw new RuntimeException("Tried to contact namingServer but ip was not yet resolved");
    }

    /**
     * Get the ip address of a given node by its hash.
     * @param nodeHash the hash of the node you want the ip from.
     * @return the ip address of the node.
     * @throws UnknownHostException thrown if the result couldn't be parsed to an ipAddress.
     */
    public Inet4Address getIp(int nodeHash) throws UnknownHostException {
       if (namingServerAddress != null) {
           String result = WebClient.create("http://" + namingServerAddress.getHostAddress() + ":54321")
                   .get()
                   .uri("/node/" + nodeHash)
                   .retrieve()
                   .bodyToMono(String.class)
                   .block();
           return (Inet4Address) Inet4Address.getByName(result);
       }
        logger.severe("NamingServer Address not yet set");
        throw new RuntimeException("Tried to contact namingServer but ip was not yet resolved");
    }

    /**
     * Finds the owner of the file given the fileHash.
     * @param fileHash the hash of the file.
     * @return InetAddress of owner.
     * @throws UnknownHostException thrown when the ipaddress couldn't be parsed.
     */
    public Inet4Address getFileOwner(int fileHash) throws UnknownHostException {
        if (namingServerAddress != null) {
            String result = WebClient.create("http://" + this.namingServerAddress.getHostAddress() + ":54321")
                    .get()
                    .uri("/file/" + fileHash)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return (Inet4Address) Inet4Address.getByName(result);
        }
        logger.severe("NamingServer Address not yet set");
        throw new RuntimeException("Tried to contact namingServer but ip was not yet resolved");
    }

    public synchronized void setNamingServerAddress(Inet4Address namingServerAddress) {
        this.namingServerAddress = namingServerAddress;
    }
}
