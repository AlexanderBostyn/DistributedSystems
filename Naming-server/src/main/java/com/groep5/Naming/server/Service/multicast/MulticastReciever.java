package com.groep5.Naming.server.Service.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Logger;

public class MulticastReciever implements Runnable {
    private volatile boolean running = true;
    private DatagramSocket socket;
    private InetAddress group;


    Logger logger = Logger.getLogger(MulticastReciever.class.getName());
    public static void main(String[] args) {
        Thread t = new Thread(new MulticastReciever());
        t.start();
    }
    public void receiveUDPMessage(int port) throws IOException {
        byte[] buffer=new byte[1024];
        socket=new DatagramSocket(port);
        while(running){
            System.out.println("Waiting for multicast message...");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(),packet.getLength());
            System.out.println("[Multicast UDP message received] >> "+msg);
            new MulticastHandler(msg).run();
        }
    }
    @Override
    public void run(){
        try {
            receiveUDPMessage( 4321);
        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            socket.close();
        }
    }
    public void terminate() {
        running = false;
    }
}