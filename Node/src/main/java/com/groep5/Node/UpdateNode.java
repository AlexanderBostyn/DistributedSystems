package com.groep5.Node;

import com.groep5.Node.Unicast.UnicastSender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UpdateNode implements Runnable{
    private String nodeName;
    private String IPAddress;
    private Node node;
    public UpdateNode(String msg, Node node) {
        String[] message = msg.split(";");
        nodeName = message[0];
        IPAddress = message[1];
        this.node = node;
    }

    private int HashNode() {
        int hash = node.calculateHash(nodeName);
        try {
            node.addNodeMap(hash, InetAddress.getByName(IPAddress));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return hash;
    }

    private void UpdateParameter(int hash) throws IOException {
        int current = node.getNodeHash();
        int previous = node.getPreviousHash();
        int next = node.getNextHash();
        if (current < hash && hash < next) {
            node.setNextHash(hash);
            UnicastSender unicastSender = new UnicastSender(current, next, IPAddress);
            unicastSender.run();
        }
        if (previous < hash && hash < current) {
            node.setPreviousHash(hash);
            UnicastSender unicastSender = new UnicastSender(previous, current, IPAddress);
            unicastSender.run();
        }
    }

    @Override
    public void run() {
        int hash = HashNode();
        try {
            UpdateParameter(hash);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
