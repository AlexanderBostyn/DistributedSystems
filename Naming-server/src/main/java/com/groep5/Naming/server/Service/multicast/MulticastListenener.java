package com.groep5.Naming.server.Service.multicast;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Logger;

public class MulticastListenener extends Thread {
    private Logger logger = Logger.getLogger(MulticastListenener.class.getName());

    private TreeMap<Integer, InetAddress> mapping;
    private MulticastSocket socket;

    public MulticastListenener(TreeMap<Integer, InetAddress> mapping) {
        this.mapping = mapping;
        try {
            this.socket = new MulticastSocket(54322);
        } catch (IOException e) {
            logger.severe("Io exception while creating multicast socket:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void run() {
        byte[] buffer = new byte[256];
        try {
            InetAddress group = InetAddress.getByName("233.0.0.0");
            socket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                logger.info("listening...");
                socket.receive(packet);
                String message = new String(packet.getData(), StandardCharsets.UTF_8);
                logger.info("Multicast received:" + message);
                logger.info("From: " + packet.getSocketAddress());
            }

        } catch (IOException e) {
            logger.severe("Io exception while trying to join MCgroup: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}

