package com.groep5.Node.Unicast;

import com.groep5.Node.Node;

import net.officefloor.plugin.variable.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
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
<<<<<<< HEAD:Node/src/main/java/com/groep5/Node/Unicast/UnicastHandler.java
                case "namingServer":
                    node.setNamingServerAddress((Inet4Address) socket.getInetAddress());
                    node.setNumberOfNodes(Integer.parseInt(message[1]));
                    break;
                default:
                    node.addNodeMap(node.calculateHash(message[0]), Inet4Address.getByName(message[1]));
                    break;
=======
                case "discovery" -> discoveryHandler(message);
                case "failure" -> failureHandler(message);
                default -> logger.info("Message could not be parsed: " + Arrays.toString(message));
>>>>>>> discovery:Node/src/main/java/com/groep5/Node/UnicastHandler.java
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

    private void failureHandler(String[] message) {
        switch (message[1]) {
            case "previous" -> {
                logger.info("previous Node failed");
                node.previousHash = Integer.parseInt(message[2]);
            }
            case "next" -> {
                logger.info("Next Node Failed");
                node.nextHash = Integer.parseInt(message[2]);
            }
            default -> logger.severe("Message could not be parsed");
        }
    }

    public void discoveryHandler(String[] message) {
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
}
