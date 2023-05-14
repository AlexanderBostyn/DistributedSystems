package com.groep5.Node.Service.Unicast.Receivers;

import java.io.File;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;
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
        //TODO
        //works together with LogSender
        return null;
    }
}
