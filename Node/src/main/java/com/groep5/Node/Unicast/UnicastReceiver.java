package com.groep5.Node.Unicast;

import com.groep5.Node.Node;
import com.groep5.Node.Unicast.UnicastHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class UnicastReceiver extends Thread {
    private Node node;
    private ServerSocket socket;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Failure failure;

    public UnicastReceiver(Node node) {
        this.node = node;
        try {
            this.socket = new ServerSocket(4321);
            logger.info("Created serverSocket:" + socket.toString());
        } catch (IOException e) {
            logger.severe("Error creating serverSocket");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                logger.info("Waiting on connection:");
                //UnicastHandler unicastHandler = new UnicastHandler(socket.accept(), node);
                new UnicastHandler(socket.accept(), node).start();
            } catch (IOException e) {
                if (!isInterrupted()) {
                    logger.severe("Error in accepting socket");
                    throw new RuntimeException(e);
                }
            }
        }
    }



    public void stopTask() {
        this.interrupt();
        try {
            socket.close();
            logger.info("closed socket");
        } catch (IOException e) {
            logger.severe("error closing socket");
            throw new RuntimeException(e);
        }
    }
}
