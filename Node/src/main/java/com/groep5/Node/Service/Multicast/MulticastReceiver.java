package com.groep5.Node.Service.Multicast;

import com.groep5.Node.Agents.FailureAgent;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.Model.Node;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.NodeLifeCycle.Replication.UpdateNewNode;
import com.groep5.Node.SpringContext;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;

@SuppressWarnings("resource")
public class MulticastReceiver extends Thread {
    private final NodePropreties nodePropreties;
    private final NamingServerService namingServerService;
    //private  final  Node node;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public MulticastReceiver() {
        this.namingServerService = NodeApplication.getNamingServerService();
        //this.node = getNode();
        this.nodePropreties = NodeApplication.getNodePropreties();
    }

    public void receiveUDPMessage() {
        byte[] buffer = new byte[1024];
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(4321);
            while (!isInterrupted()) {
                logger.info("waiting for multicast message");
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                logger.info("Multicast Received: " + msg);
                new MulticastReceiverHandler(msg).start();
            }
        } catch (IOException e) {
            logger.severe("Error creating multicastReceiver socket");
            new FailureAgent().startFailureAgent();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        receiveUDPMessage();
    }

    private class MulticastReceiverHandler extends Thread {
        private final String msg;

        MulticastReceiverHandler(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                logger.info("Stopping Failure and Sync task");
                nodePropreties.stopFailure();
                nodePropreties.stopUpdateLogTask();
                String[] splitMessage = msg.split(";");
                if (splitMessage[0].equals("deletion")) {
                    NodeApplication.getLog().delete(splitMessage[1]);
                    File file = new File("src/main/resources/replicated/" + splitMessage[1]);
                    if (file.delete()) {
                        logger.info("file " + file + " is deleted");
                    }
                    else {
                        logger.severe("error: " + file + " could not be deleted");
                    }
                }
                if (!splitMessage[0].equals("discovery")) return;
                int receivedNodeHash = namingServerService.calculateHash(splitMessage[1]);
                InetAddress address = InetAddress.getByName(splitMessage[2]);
                logger.info("Received message: " + Arrays.toString(splitMessage));
                logger.info("receivedNodeHash < node.nextHash " + (receivedNodeHash < nodePropreties.nextHash));
                logger.info("receivedNodeHash > node.previousHash " + (receivedNodeHash > nodePropreties.previousHash));
                //check if node is first or last in the ring
                if (nodePropreties.previousHash == nodePropreties.nextHash && nodePropreties.previousHash == nodePropreties.nodeHash) {
                    logger.info("The current network size was one, new node is next and previous");
                    nodePropreties.nextHash = receivedNodeHash;
                    nodePropreties.previousHash = receivedNodeHash;
                    sendMessage("discovery;next;" + nodePropreties.nodeHash, address);
                    sendMessage("discovery;previous;" + nodePropreties.nodeHash, address);
                }
                else if (nodePropreties.nodeHash > nodePropreties.nextHash) { //last node in ring
                    logger.info("We are the last node in the ring");
                    if (receivedNodeHash > nodePropreties.nodeHash || receivedNodeHash < nodePropreties.nextHash) {
                        //because this is the last ring, if the received hash is bigger than itself or smaller than the next hash it must be the new next node
                        logger.info("received node is the new nextNode: " + receivedNodeHash);
                        //send files that now belong to new node to new node
                        nodePropreties.nextHash = receivedNodeHash;
                        sendMessage("discovery;previous;" + nodePropreties.nodeHash, address);
                    } else if (receivedNodeHash > nodePropreties.previousHash && receivedNodeHash < nodePropreties.nodeHash) {
                        //if the new hash is smaller than the current hash but bigger than the previous hash than it becomes the new previous hash.
                        logger.info("received node is the new previousNode: " + receivedNodeHash);
                        nodePropreties.previousHash = receivedNodeHash;
                        sendMessage("discovery;next;" + nodePropreties.nodeHash, address);
                    }
                }
                else if(nodePropreties.nodeHash < nodePropreties.previousHash) { //first node in ring
                    if (receivedNodeHash < nodePropreties.nextHash && receivedNodeHash > nodePropreties.nodeHash) {
                        //if the received hash is smaller than the nexthash but bigger than the next hash it must be the new next node
                        logger.info("received node is the new nextNode: " + receivedNodeHash);
                        //send files that now belong to new node to new node
                        nodePropreties.nextHash = receivedNodeHash;
                        sendMessage("discovery;previous;" + nodePropreties.nodeHash, address);
                    } else if (receivedNodeHash < nodePropreties.nodeHash || receivedNodeHash > nodePropreties.nextHash) {
                        //because this is the first node in the ring, if the received hash is smaller than itself or bigger than the nexthash it must be the new previous node.
                        logger.info("received node is the new previousNode: " + receivedNodeHash);
                        nodePropreties.previousHash = receivedNodeHash;
                        sendMessage("discovery;next;" + nodePropreties.nodeHash, address);
                    }
                } else { //must be a normal node
                    if (receivedNodeHash < nodePropreties.nextHash && receivedNodeHash > nodePropreties.nodeHash) {
                        //if the received hash is smaller than the nexthash but bigger than the next hash it must be the new next node
                        logger.info("received node is the new nextNode: " + receivedNodeHash);
                        nodePropreties.nextHash = receivedNodeHash;
                        sendMessage("discovery;previous;" + nodePropreties.nodeHash, address);
                    } else if (receivedNodeHash > nodePropreties.previousHash && receivedNodeHash < nodePropreties.nodeHash) {
                        //if the received hash is bigger than the previous hash but smaller than self than it must be the new previous node
                        logger.info("received node is the new previousNode: " + receivedNodeHash);
                        nodePropreties.previousHash = receivedNodeHash;
                        sendMessage("discovery;next;" + nodePropreties.nodeHash, address);
                    }
                }
                logger.info("Parameters set: ");
                logger.info("previousHash: " + nodePropreties.previousHash);
                logger.info("nodeHash: " + nodePropreties.nodeHash);
                logger.info("nextHash: " + nodePropreties.nextHash);
                Thread.sleep(5000);
                logger.info("restarting Failure and sync thread");
                nodePropreties.startNewFailure();
                nodePropreties.startNewUpdateLogTask();

                new UpdateNewNode( receivedNodeHash);
                //Our nextNode was updated
                /*if (nodePropreties.nextHash == receivedNodeHash) {
                    logger.info("start updating nodes");
                    new UpdateNewNode( receivedNodeHash);
                }

                 */

            } catch (UnknownHostException e) {
                logger.severe("InetAddress not found");
                throw new RuntimeException(e);
            } catch (IOException e) {
                new FailureAgent().startFailureAgent();
                logger.severe("couldn't open unicast socket");
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private synchronized void sendMessage(String message, InetAddress address) throws IOException {
        logger.info("sending message: " + message + ", to: " + address.getHostAddress());
        Socket socket = new Socket(address, 4321);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println(message);
        socket.close();
    }
}
