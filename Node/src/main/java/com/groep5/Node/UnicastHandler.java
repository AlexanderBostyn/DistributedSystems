package com.groep5.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class UnicastHandler implements Runnable{
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Socket socket;
    private Node node;

    public UnicastHandler(Socket socket, Node node) {
       this.socket = socket;
       this.node = node;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            logger.severe("Idk wat er gebeurt is but you fucked up");
            throw new RuntimeException(e);
        }
    }
}
