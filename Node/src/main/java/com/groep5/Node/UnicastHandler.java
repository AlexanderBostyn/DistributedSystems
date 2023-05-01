package com.groep5.Node;

import net.officefloor.plugin.variable.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

public class UnicastHandler extends Thread {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Socket socket;
    private Node node;

    public UnicastHandler(Socket socket, Node node) {
        logger.info("Received connection");
        this.socket = socket;
        this.node = node;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String[] message = input.readLine().split(";");
            logger.info("message received:" + Arrays.toString(message));
            switch (message[0]) {
                case "discovery" -> discoveryHandler(message);
                case "failure" -> failureHandler(message);
                case "shutdown" -> shutdownHandler(message);
                default -> logger.info("Message could not be parsed: " + Arrays.toString(message));
            }
            socket.close();
            node.finishConnection();
        } catch (NullPointerException e) {
            logger.info("Got pinged");
        } catch (IOException e) {
            logger.severe("Idk wat er gebeurd is but you fucked up");
            throw new RuntimeException(e);
        }
    }

    private synchronized void failureHandler(String[] message) {
        switch (message[1]) {
            case "previous" -> {
                logger.info("previous Node failed");
                if (node.previousHash != Integer.parseInt(message[2])) {
                    node.previousHash = Integer.parseInt(message[2]);
                    try {
                        node.sendUnicast("failure;next;" + node.nodeHash, new InetSocketAddress(socket.getInetAddress(), 4321));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case "next" -> {
                logger.info("Next Node Failed");
                if (node.nextHash != Integer.parseInt(message[2])) {
                    node.nextHash = Integer.parseInt(message[2]);
                    try {
                        node.sendUnicast("failure;previous;" + node.nodeHash, new InetSocketAddress(socket.getInetAddress(), 4321));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            default -> logger.severe("Message could not be parsed");
        }
        logger.info("Parameters set: ");
        logger.info("previousHash: " + node.previousHash);
        logger.info("nodeHash: " + node.nodeHash);
        logger.info("nextHash: " + node.nextHash);
    }

    private synchronized void discoveryHandler(String[] message) {
        switch (message[1]) {
            case "namingServer" -> {
                logger.info("location of namingServer: " + socket.getInetAddress());
                node.setNamingServerAddress((Inet4Address) socket.getInetAddress());
//                    node.setNumberOfNodes(Integer.parseInt(message[1]));
                node.setNumberOfNodes(Integer.parseInt(message[2]));
            }
            case "previous" -> {
                logger.info("previous Node at: " + socket.getInetAddress() + ", with hash: " + message[1]);
                node.previousHash = Integer.parseInt(message[2]);
            }
            case "next" -> {
                logger.info("next Node at: " + socket.getInetAddress() + ", with hash: " + message[1]);
                node.nextHash = Integer.parseInt(message[2]);
            }
            default -> logger.info("Message could not be parsed: " + Arrays.toString(message));
        }
    }

    private synchronized void shutdownHandler(String[] message) {
        switch (message[1]) {
            case "previous" -> {
                node.previousHash = Integer.parseInt(message[2]);
            }
            case "next" -> {
                node.nextHash = Integer.parseInt(message[2]);
            }
            default -> logger.warning("Message could not be parsed: " + Arrays.toString(message));
        }
        logger.info("Parameters set: ");
        logger.info("previousHash: " + node.previousHash);
        logger.info("nodeHash: " + node.nodeHash);
        logger.info("nextHash: " + node.nextHash);
    }
}
