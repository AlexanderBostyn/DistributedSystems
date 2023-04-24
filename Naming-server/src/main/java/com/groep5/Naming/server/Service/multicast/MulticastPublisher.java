package com.groep5.Naming.server.Service.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastPublisher implements Runnable {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;

    public void sendUDPMessage(String message, String ipAddress, int port) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName(ipAddress);
        buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
        socket.send(packet);
        socket.close();
    }
    @Override
    public void run() {
        try {
            sendUDPMessage("This is a multicast messge", "238.0.0.0", 4321);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}