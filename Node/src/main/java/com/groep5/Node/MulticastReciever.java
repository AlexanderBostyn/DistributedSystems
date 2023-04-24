package com.groep5.Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReciever implements Runnable {
    public static void main(String[] args) {
        Thread t = new Thread(new MulticastReciever());
        t.start();
    }

    public void receiveUDPMessage(String ip, int port) throws IOException {
        byte[] buffer = new byte[1024];
        MulticastSocket socket = new MulticastSocket(4321);
        InetAddress group = InetAddress.getByName("238.0.0.0");
        socket.joinGroup(group);
        while(true){
            System.out.println("Waiting for multicast message...");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(),packet.getLength());
            System.out.println("[Multicast UDP message received] >> "+msg);
            //if node leaves --> break
        }
        /*socket.leaveGroup(group);
        socket.close();*/
    }

    @Override
    public void run(){
        try {
            receiveUDPMessage("238.0.0.0", 4321);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
