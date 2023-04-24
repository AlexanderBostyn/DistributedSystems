package com.groep5.Node.Multicast;

import com.groep5.Node.Node;
import com.groep5.Node.Unicast.UnicastSender;
import com.groep5.Node.UpdateNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReciever implements Runnable {
    private static Node node;
    public static void main(String[] args) {
        Thread t = new Thread(new MulticastReciever(node));
        t.start();
    }

    public MulticastReciever(Node node) {
        this.node = node;
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
            UpdateNode updateNode = new UpdateNode(msg, node);
            updateNode.run();
        }
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
