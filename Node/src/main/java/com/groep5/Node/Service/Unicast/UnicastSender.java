package com.groep5.Node.Service.Unicast;

import com.groep5.Node.Model.Log;
import com.groep5.Node.Service.Unicast.Senders.FileSender;
import com.groep5.Node.Service.Unicast.Senders.LogSender;
import com.groep5.Node.Service.Unicast.Senders.MessageSender;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that covers all our Unicast (TCP) sending needs.
 * Port 4321 is always used.
 */
public class UnicastSender {

    /**
     * Sends a message over TCP synchronously.
     *
     * @param message     message you want to send
     * @param destination the destination ip-address
     * @throws IOException when an error occurred in creating a socket.
     */
    public static synchronized void sendMessage(String message, Inet4Address destination) throws IOException {
        new MessageSender(message, destination).send();
    }

    /**
     * Sends a file in a thread using {@link FileSender}.
     *
     * @param file        the file that needs to be sent.
     * @param destination the destination ip.
     * @return A reference to the fileSender thread.
     */
    public static FileSender sendFile(File file, Inet4Address destination) {
        FileSender fileSender = new FileSender(file, destination);
        fileSender.start();
        return fileSender;
    }

    ;

    /**
     * Sends a log to the destination in a thread using {@link com.groep5.Node.Service.Unicast.Senders.LogSender}
     *
     * @param log         the log we need to send. A map of file keys with Arraylists of Inet4Addresses as values.
     * @param destination the destination ip.
     */
    public static void sendLog(Log log, Inet4Address destination) {
        new LogSender(log, destination).start();
    }

    ;

}
