package com.groep5.Naming.server.Service;

import com.groep5.Naming.server.Service.multicast.MulticastHandler;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Logger;

public class UnicastPublisher {
    private static Logger logger = Logger.getLogger(UnicastPublisher.class.getName());

    public static void sendMessage(String message, InetAddress address) {
        try {
            logger.info("sending '" + message + "' to address: "+address.toString());
            Socket socket = new Socket(address, 4321); // replace 12345 with the desired destination port
            OutputStream outputStream = socket.getOutputStream();
            /*outputStream.write(message.getBytes());
            outputStream.flush();
            outputStream.close();
            socket.close();
             */
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(message);
            socket.close();//end connection

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error sending TCP message: " + e.getMessage() );
        }
    }
}
