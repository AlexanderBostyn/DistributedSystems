package com.groep5.Node.Multicast;

import com.groep5.Node.Failure;
import com.groep5.Node.Node;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;

public class MulticastReceiver extends Thread {
    private Node node;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public MulticastReceiver(Node node ) {
        this.node = node;
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
            throw new RuntimeException(e);
        }
        /*socket.leaveGroup(group);
        socket.close();*/
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
                logger.info("Stopping Failure task");
                node.getFailure().stop();
                String[] splitMessage = msg.split(";");
                if (!splitMessage[0].equals("discovery")) return;
                int receivedNodeHash = node.calculateHash(splitMessage[1]);
                InetAddress address = InetAddress.getByName(splitMessage[2]);
                logger.info("Received message: " + Arrays.toString(splitMessage));
                logger.info("receivedNodeHash < node.nextHash " + (receivedNodeHash < node.nextHash));
                logger.info("receivedNodeHash > node.previousHash " + (receivedNodeHash > node.previousHash));
                //check if node is first or last in the ring
                if (node.previousHash == node.nextHash && node.previousHash == node.nodeHash) {
                    logger.info("The current network size was one, new node is next and previous");
                    node.nextHash = receivedNodeHash;
                    node.previousHash = receivedNodeHash;
                    sendMessage("discovery;next;" + node.nodeHash, address);
                    sendMessage("discovery;previous;" + node.nodeHash, address);
                }
                else if (node.nodeHash > node.nextHash) { //last node in ring
                    logger.info("We are the last node in the ring");
                    if (receivedNodeHash > node.nodeHash || receivedNodeHash < node.nextHash) {
                        //because this is the last ring, if the received hash is bigger than itself or smaller than the next hash it must be the new next node
                        logger.info("received node is the new nextNode: " + receivedNodeHash);
                        node.nextHash = receivedNodeHash;
                        sendMessage("discovery;previous;" + node.nodeHash, address);
                    } else if (receivedNodeHash > node.previousHash && receivedNodeHash < node.nodeHash) {
                        //if the new hash is smaller than the current hash but bigger than the previous hash than it becomes the new previous hash.
                        logger.info("received node is the new previousNode: " + receivedNodeHash);
                        node.previousHash = receivedNodeHash;
                        sendMessage("discovery;next;" + node.nodeHash, address);
                    }
                }
                else if(node.nodeHash < node.previousHash) { //first node in ring
                    if (receivedNodeHash < node.nextHash && receivedNodeHash > node.nodeHash) {
                        //if the received hash is smaller than the nexthash but bigger than the next hash it must be the new next node
                        logger.info("received node is the new nextNode: " + receivedNodeHash);
                        node.nextHash = receivedNodeHash;
                        sendMessage("discovery;previous;" + node.nodeHash, address);
                    } else if (receivedNodeHash < node.nodeHash || receivedNodeHash > node.nextHash) {
                        //because this is the first node in the ring, if the received hash is smaller than itself or bigger than the nexthash it must be the new previous node.
                        logger.info("received node is the new previousNode: " + receivedNodeHash);
                        node.previousHash = receivedNodeHash;
                        sendMessage("discovery;next;" + node.nodeHash, address);
                    }
                } else { //must be a normal node
                    if (receivedNodeHash < node.nextHash && receivedNodeHash > node.nodeHash) {
                        //if the received hash is smaller than the nexthash but bigger than the next hash it must be the new next node
                        logger.info("received node is the new nextNode: " + receivedNodeHash);
                        node.nextHash = receivedNodeHash;
                        sendMessage("discovery;previous;" + node.nodeHash, address);
                    } else if (receivedNodeHash > node.previousHash && receivedNodeHash < node.nodeHash) {
                        //if the received hash is bigger than the previous hash but smaller than self than it must be the new previous node
                        logger.info("received node is the new previousNode: " + receivedNodeHash);
                        node.previousHash = receivedNodeHash;
                        sendMessage("discovery;next;" + node.nodeHash, address);
                    }
                }
                logger.info("Parameters set: ");
                logger.info("previousHash: " + node.previousHash);
                logger.info("nodeHash: " + node.nodeHash);
                logger.info("nextHash: " + node.nextHash);
                Thread.sleep(5000);
                logger.info("restarting Failure thread");
                node.setFailure(new Failure(node));
                node.getFailure().start();
            } catch (UnknownHostException e) {
                logger.severe("InetAddress not found");
                throw new RuntimeException(e);
            } catch (IOException e) {
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
