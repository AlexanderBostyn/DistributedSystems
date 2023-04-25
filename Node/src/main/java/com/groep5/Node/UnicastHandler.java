package com.groep5.Node;

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

public class UnicastHandler extends Thread{
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
                case "namingServer":
                    logger.info("location of namingServer: " + socket.getInetAddress());
                    node.setNamingServerAddress((Inet4Address) socket.getInetAddress());
//                    node.setNumberOfNodes(Integer.parseInt(message[1]));
                    node.setNumberOfNodes(Integer.parseInt(message[1]));
                    break;
                case "previous":
                    logger.info("previous Node at: " + socket.getInetAddress() + ", with hash: " + message[1]);
                    node.previousHash = Integer.parseInt(message[1]);
                    break;
                case "next":
                    logger.info("next Node at: " + socket.getInetAddress() + ", with hash: " + message[1]);
                    node.nextHash = Integer.parseInt(message[1]);
                    break;
                default:
                    logger.info("Message could not be parsed: " + Arrays.toString(message));
            }
            socket.close();
            node.finishConnection();
        } catch (IOException e) {
            logger.severe("Idk wat er gebeurd is but you fucked up");
            throw new RuntimeException(e);
        }
    }
}
