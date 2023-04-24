package com.groep5.Naming.server.Service.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Logger;

public class MulticastReciever implements Runnable {
    private volatile boolean running = true;
    private MulticastSocket socket;
    private InetAddress group;


    Logger logger = Logger.getLogger(MulticastReciever.class.getName());
    public static void main(String[] args) {
        Thread t = new Thread(new MulticastReciever());
        t.start();
    }
    public void receiveUDPMessage(String ip, int port) throws IOException {
        byte[] buffer=new byte[1024];
        socket=new MulticastSocket(port);
        group=InetAddress.getByName(ip);
        socket.joinGroup(group);
        while(running){
            System.out.println("Waiting for multicast message...");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(),packet.getLength());
            System.out.println("[Multicast UDP message received] >> "+msg);
            if("OK".equals(msg)) {
                System.out.println("No more message. Exiting : "+msg);
                break;
            }
            new MulticastHandler(msg).run();
            //if node leaves --> break
        }
    }
    @Override
    public void run(){
        try {
            receiveUDPMessage("238.0.0.0", 4321);
        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            try {
                socket.leaveGroup(group);
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void terminate() {
        running = false;
    }
}