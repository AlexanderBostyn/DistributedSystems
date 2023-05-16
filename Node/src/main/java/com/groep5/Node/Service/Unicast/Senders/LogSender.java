package com.groep5.Node.Service.Unicast.Senders;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Sends a log to a destination.
 * The message should start with: "log;{entrySet.length}"
 */
public class LogSender extends Thread{
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final HashMap<File, ArrayList<Inet4Address>> log;
    private final Inet4Address destination;

    public LogSender(HashMap<File, ArrayList<Inet4Address>> log, Inet4Address destination) {
        this.log = log;
        this.destination = destination;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(destination, 4321);
            if (this.log != null) {

                logger.info("Sending log to: " + destination);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                logger.info("log size is : "+log.size());
                logger.info("log: " + log.entrySet().toString());
                printWriter.println("log;" +  log.size() +";");
                // Serialize the HashMap into a byte array
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                oos.writeObject(log);
                oos.flush();
                socket.getOutputStream().flush();
                socket.close();

                logger.info("Finished sending log to: "+destination );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
