package com.groep5.Node.Service.Unicast;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class UnicastReceiver extends Thread {

    private final ServerSocket socket;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public UnicastReceiver() {
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
                logger.fine("Waiting on connection:");
                new UnicastHandler(socket.accept()).start();
            } catch (IOException e) {
                if (!isInterrupted()) {
                    logger.severe("Error in accepting socket");
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
