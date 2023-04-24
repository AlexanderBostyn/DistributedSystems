package com.groep5.Node;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.logging.Logger;

public class MulticastReceiver extends Thread {
    private Node node;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public MulticastReceiver(Node node) {
        this.node = node;
    }

    public void receiveUDPMessage(String ip, int port) {
        byte[] buffer = new byte[1024];
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(4321);
            InetAddress group = InetAddress.getByName("238.0.0.0");
            socket.joinGroup(group);
            while (!isInterrupted()) {
                logger.info("waiting for multicast message");
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                logger.info("Multicast Received: " + msg);
                new MulticastReceiverHandler(msg).run();
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
        receiveUDPMessage("238.0.0.0", 4321);
    }

    private class MulticastReceiverHandler extends Thread {
        private final String msg;

        MulticastReceiverHandler(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                String[] splitMessage = msg.split(";");
                int receivedNodeHash = node.calculateHash(splitMessage[0]);
                InetAddress address = InetAddress.getByName(splitMessage[1]);
                String newMessage = "";
                if (receivedNodeHash < node.nextHash && receivedNodeHash > node.nodeHash) {
                    //if the new hash is bigger than the current hash but smaller than the next hash than it becomes the new next hash.
                    logger.info("received node is the new nextNode: " + receivedNodeHash);
                    node.nextHash = receivedNodeHash;
                    newMessage = "previous;" + node.nodeHash;
                }
                else if (receivedNodeHash > node.previousHash && receivedNodeHash < node.nodeHash) {
                    //if the new hash is smaller than the current hash but bigger than the previous hash than it becomes the new previous hash.
                    logger.info("received node is the new previousNode: " + receivedNodeHash);
                    node.previousHash = receivedNodeHash;
                    newMessage = "next;" + node.nodeHash;
                }
                if (!newMessage.isEmpty()) {
                    Socket socket = new Socket(address, 4321);
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
                    writer.println(newMessage);
                    logger.info("notified node");
                }
            } catch (UnknownHostException e) {
                logger.severe("InetAddress not found");
                throw new RuntimeException(e);
            } catch (IOException e) {
                logger.severe("couldn't open unicast socket");
                throw new RuntimeException(e);
            }

        }
    }
}
