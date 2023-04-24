package com.groep5.Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

public class MulticastSender extends Thread{
    private final String message;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public static void sendUDPMessage(String message, String ipAddress, int port) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        InetAddress group = InetAddress.getByName(ipAddress);
        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);
        socket.send(packet);
        socket.close();
    }

    /*public static void main(String[] args) throws IOException {
        sendUDPMessage("This is a multicast messge", "238.0.0.0", 4321);
        sendUDPMessage("This is the second multicast messge", "238.0.0.0", 4321);
        sendUDPMessage("This is the third multicast messge", "238.0.0.0", 4321);
        //sendUDPMessage("OK", "238.0.0.0", 4321);
    }*/

//    public static void main(String[] args) {
//        Thread t = new Thread(new MulticastSender());
//        t.start();
//    }

    public MulticastSender(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            sendUDPMessage(message, "255.255.255.255", 4321);
            logger.info("Send multicast message: " + message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
