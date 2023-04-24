package com.groep5.Node;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class UnicastReceiver implements Runnable{
    private Node node;
    private ServerSocket socket;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public UnicastReceiver(Node node) {
        this.node = node;
        try {
            this.socket = new ServerSocket(4321);
        } catch (IOException e) {
            logger.severe("Error creating serverSocket");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                UnicastHandler unicastHandler = new UnicastHandler(socket.accept(), node);
            } catch (IOException e) {
                logger.severe("Error in accepting socket");
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.severe("error closing socket");
            throw new RuntimeException(e);
        }
    }
}
