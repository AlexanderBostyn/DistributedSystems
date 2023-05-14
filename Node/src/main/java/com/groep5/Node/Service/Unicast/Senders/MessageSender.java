package com.groep5.Node.Service.Unicast.Senders;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Sends messages over TCP.
 * Ideally used for one-liners
 */
public class MessageSender {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final String message;
    private final Inet4Address destination;

    public MessageSender(String message, Inet4Address destination) {
        this.message = message;
        this.destination = destination;
    }

    public void send() throws IOException {
        logger.info("Sending Unicast to" + destination + ", message: " + message);
        Socket socket = new Socket(destination, 4321);
        PrintWriter printer = new PrintWriter(socket.getOutputStream(), true);
        printer.println(message);
        socket.close();
    }
}
