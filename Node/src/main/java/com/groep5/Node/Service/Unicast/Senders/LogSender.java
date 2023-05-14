package com.groep5.Node.Service.Unicast.Senders;

import java.io.File;
import java.net.Inet4Address;
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
        //TODO
    }
}
