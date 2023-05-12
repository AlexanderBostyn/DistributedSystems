package com.groep5.Node.Service.Unicast;

import com.groep5.Node.Failure;
import com.groep5.Node.Node;

import com.groep5.Node.SpringContext;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

public class UnicastHandler extends Thread {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Socket socket;
    private final Node node;
    private Node getNode() {
        return SpringContext.getBean(Node.class);
    }

    public UnicastHandler(Socket socket) {
        logger.fine("Received connection");
        this.socket = socket;
        this.node= getNode();
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
                        node.sendUnicast("failure;next;" + node.nodeHash, new InetSocketAddress(socket.getInetAddress(), 4321));
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
        Failure failure = node.getFailure();
        if (failure!= null && failure.isAlive()) failure.stop();
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
        String filename = message[1];
        long size = Long.parseLong(message[2]);
        File file = new File("src/main/resources/replicas" + filename);
        int bytes = 0;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            byte[] buffer = new byte[4*1024];
            while (size > 0 && (bytes = dis.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0,bytes);
                size -= bytes;
            }
            dis.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            InetAddress fileOwner = node.findNodeOwner(node.calculateHash(filename));
            if (fileOwner.getHostAddress().equals(node.getNodeAddress().getHostAddress())) {
                node.addLog(file, fileOwner.getHostAddress());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
