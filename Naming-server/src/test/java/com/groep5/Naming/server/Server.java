package com.groep5.Naming.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server implements Runnable{

    public static void sendUDPMessage(String message, String ipAddress, int port) throws IOException {
        DatagramSocket socket = new DatagramSocket();
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

    public static void main(String[] args) {
        Thread t = new Thread(new Server());
        t.start();
    }

    @Override
    public void run() {
        try {
            System.out.println("test");
            sendUDPMessage("This is a multicast messge", "238.0.0.0", 4321);
            sendUDPMessage("This is the second multicast messge", "238.0.0.0", 4321);
            sendUDPMessage("This is the third multicast messge", "238.0.0.0", 4321);
            sendUDPMessage("OK", "238.0.0.0", 4321);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}