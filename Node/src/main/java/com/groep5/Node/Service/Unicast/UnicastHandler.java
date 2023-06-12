package com.groep5.Node.Service.Unicast;

import com.groep5.Node.Model.Log;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NodeLifeCycle.Failure;
import com.groep5.Node.Model.Node;

import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.NodeLifeCycle.Replication.ReplicationService;
import com.groep5.Node.Service.Unicast.Receivers.FileReceiver;
import com.groep5.Node.Service.Unicast.Receivers.LogReceiver;
import com.groep5.Node.SpringContext;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UnicastHandler extends Thread {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Socket socket;
    private final NodePropreties nodePropreties;
    private final NamingServerService namingServerService;
    private final ReplicationService replicationService;
    private final Log log;


    public UnicastHandler(Socket socket) {
        logger.fine("Received connection");
        this.socket = socket;
        this.nodePropreties= NodeApplication.getNodePropreties();
        this.namingServerService = NodeApplication.getNamingServerService();
        this.replicationService = NodeApplication.getReplicationService();
        this.log = NodeApplication.getLog();
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
                case "failureAgent"->
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

    private synchronized void failureHandler(String[] message) {//message[2] contains the new value for prev/next node
        nodePropreties.stopFailure();
       //node.getFailure().stop();
        switch (message[1]) {
            case "previous" -> {
                logger.severe("previous Node failed");
                if (nodePropreties.previousHash != Integer.parseInt(message[2])) {
                    nodePropreties.previousHash = Integer.parseInt(message[2]);
                    try {
                        UnicastSender.sendMessage("failure;next;" + nodePropreties.nodeHash, (Inet4Address) socket.getInetAddress());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case "next" -> {
                logger.severe("Next Node Failed");
                if (nodePropreties.nextHash != Integer.parseInt(message[2])) {
                    nodePropreties.nextHash = Integer.parseInt(message[2]);
                    try {
                        UnicastSender.sendMessage("failure;previous;" + nodePropreties.nodeHash, (Inet4Address) socket.getInetAddress());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            default -> logger.severe("Message could not be parsed");
        }
        logger.info("Parameters set: ");
        logger.info("previousHash: " + nodePropreties.previousHash);
        logger.info("nodeHash: " + nodePropreties.nodeHash);
        logger.info("nextHash: " + nodePropreties.nextHash);
    }

    private synchronized void discoveryHandler(String[] message) {
        Failure failure = nodePropreties.getFailure();
        if (failure!= null && failure.isAlive()) nodePropreties.stopFailure();;
        switch (message[1]) {
            case "namingServer" -> {
                logger.info("location of namingServer: " + socket.getInetAddress());
                namingServerService.setNamingServerAddress((Inet4Address) socket.getInetAddress());
                nodePropreties.setNumberOfNodes(Integer.parseInt(message[2]));
            }
            case "previous" -> {
                logger.info("previous Node at: " + socket.getInetAddress() + ", with hash: " + message[1]);
                nodePropreties.previousHash = Integer.parseInt(message[2]);
            }
            case "next" -> {
                logger.info("next Node at: " + socket.getInetAddress() + ", with hash: " + message[1]);
                nodePropreties.nextHash = Integer.parseInt(message[2]);
            }
            default -> logger.severe("Message could not be parsed: " + Arrays.toString(message));
        }
        nodePropreties.finishConnection();
        if (nodePropreties.getFailure() != null) {
            nodePropreties.setFailure(new Failure());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            nodePropreties.startNewFailure();
        }
    }

    private synchronized void shutdownHandler(String[] message) {
        //node.getFailure().stop();
        nodePropreties.stopFailure();
        switch (message[1]) {
            case "previous" -> {
                nodePropreties.previousHash = Integer.parseInt(message[2]);
            }
            case "next" -> {
                nodePropreties.nextHash = Integer.parseInt(message[2]);
            }
            case "file" -> {
                //The sending node has been shutdown, meaning their location should be deleted from the log.
                Log.LogEntry entry = log.get(message[2]);
                boolean isDeleted = entry.delete((Inet4Address) socket.getInetAddress());
                if (!isDeleted) {
                    logger.severe("Received shutdown message from node to update our fileLog, but our log didn't contain that entry");
                }
                ArrayList<File> files = ReplicationService.listDirectory("src/main/resources/local");
                files.addAll(ReplicationService.listDirectory("src/main/resources/replicated"));
                if(files.stream().anyMatch(file -> file.getName().equals(message[2]))) {
                    //if we have the file in our directory we will add it to our log.
                    log.add(message[2], nodePropreties.getNodeAddress());
                };

            }
            default -> logger.warning("Message could not be parsed: " + Arrays.toString(message));
        }
        logger.info("Parameters set: ");
        logger.info("previousHash: " + nodePropreties.previousHash);
        logger.info("nodeHash: " + nodePropreties.nodeHash);
        logger.info("nextHash: " + nodePropreties.nextHash);
        nodePropreties.startNewFailure();
    }

    private void replicationHandler(String[] message) throws UnknownHostException {
        Inet4Address ip = (Inet4Address) socket.getInetAddress();
        File file = new FileReceiver(message, socket).receive();

        //If we are the owner of the file, indicated by namingserver we should add the file to our log
        if( ReplicationService.isOwner(file.getName(), this.nodePropreties.nodeHash) ) {
            log.add(file.getName(),nodePropreties.getNodeAddress());
            
            if (message[3].equals("false")) {
                //But if the place where the file came from is deleting the file immediately after we shouldnt log the receiving ip
                log.add(file.getName(),ip);
            }
        }
    }

    private void logHandler(String[] message) {
        //Combine incoming logs into our log.
        Log incomingLog = new LogReceiver(message, socket).receive();
        incomingLog.getEntrySet().forEach(log::add);
    }
}
