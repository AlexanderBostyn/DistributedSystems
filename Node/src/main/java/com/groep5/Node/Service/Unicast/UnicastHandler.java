package com.groep5.Node.Service.Unicast;

import com.groep5.Node.Failure;
import com.groep5.Node.Node;

import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.Replication.Replication;
import com.groep5.Node.Service.Unicast.Receivers.FileReceiver;
import com.groep5.Node.Service.Unicast.Receivers.LogReceiver;
import com.groep5.Node.SpringContext;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

public class UnicastHandler extends Thread {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Socket socket;
    private final Node node;
    private final NamingServerService namingServerService;
    private Node getNode() {
        return SpringContext.getBean(Node.class);
    }

    public UnicastHandler(Socket socket) {
        logger.fine("Received connection");
        this.socket = socket;
        this.node= getNode();
        this.namingServerService = node.getNamingServerService();
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
                case "replication" -> replicationHandler(message);
                case "log" -> logHandler(message);
                default -> logger.info("Message could not be parsed: " + Arrays.toString(message));
            }
            socket.close();
        } catch (NullPointerException e) {
            logger.fine("Got pinged");
        } catch (IOException e) {
            logger.severe("Idk wat er gebeurd is but you fucked up");
            throw new RuntimeException(e);
        }
    }

    private synchronized void failureHandler(String[] message) {
       node.getFailure().stop();
        switch (message[1]) {
            case "previous" -> {
                logger.severe("previous Node failed");
                if (node.previousHash != Integer.parseInt(message[2])) {
                    node.previousHash = Integer.parseInt(message[2]);
                    try {
                        UnicastSender.sendMessage("failure;next;" + node.nodeHash, (Inet4Address) socket.getInetAddress());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case "next" -> {
                logger.severe("Next Node Failed");
                if (node.nextHash != Integer.parseInt(message[2])) {
                    node.nextHash = Integer.parseInt(message[2]);
                    try {
                        UnicastSender.sendMessage("failure;previous;" + node.nodeHash, (Inet4Address) socket.getInetAddress());
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
        Failure failure = node.getFailure();
        if (failure!= null && failure.isAlive()) failure.stop();
        switch (message[1]) {
            case "namingServer" -> {
                logger.info("location of namingServer: " + socket.getInetAddress());
                node.getNamingServerService().setNamingServerAddress((Inet4Address) socket.getInetAddress());
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
            default -> logger.severe("Message could not be parsed: " + Arrays.toString(message));
        }
        node.finishConnection();
        if (node.getFailure() != null) {
            node.setFailure(new Failure(node));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            node.getFailure().start();
        }
    }


    private synchronized void shutdownHandler(String[] message) {
        node.getFailure().stop();
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
        node.setFailure(new Failure(node));
        node.getFailure().start();
    }

    private void replicationHandler(String[] message) {
        Inet4Address ip = (Inet4Address) socket.getInetAddress();
        File file = new FileReceiver(message, socket).receive();

        //If we are the owner of the file, indicated by namingserver we should at the file to our log
        if (new Replication().isOwner(file.getName())) {
            node.addLog(file, ip);
        }
    }

    private void logHandler(String[] message) {
        HashMap<File, ArrayList<Inet4Address>> log = new LogReceiver(message, socket).receive();
        //TODO add to the node's log.
    }
}
