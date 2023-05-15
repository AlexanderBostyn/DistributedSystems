package com.groep5.Node.Service.Unicast.Receivers;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This class handles the socket stream as a log Hashmap(File, ArrayList(Inet4Address))
 */
public class LogReceiver {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * has the form: ["log", entrySet.length]
     */
    private final String[] message;
    private final Socket socket;

    public LogReceiver(String[] message, Socket socket) {
        this.message = message;
        this.socket = socket;
    }

    public HashMap<File, ArrayList<Inet4Address>> receive() {
        int size = Integer.parseInt(message[1]);
        logger.info("log size is: "+size);
        int bytes = 0;
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            HashMap<File, ArrayList<Inet4Address>> log = (HashMap<File, ArrayList<Inet4Address>>) ois.readObject();
            return log;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
