package com.groep5.Node.Service.Replication;

import com.groep5.Node.Node;
import com.groep5.Node.NodeApplication;

import java.net.InetAddress;

public class Replication {
    private Node node = NodeApplication.getNode();

    /**
     * This helper function should determine where a file needs to be sent to.
     * This is based on the hash o filename and the namingServer GET /file/hash
     * If the namingServer responds with our own ip, we should send it to our previous
     * @param fileName the name of the file;
     * @return the ip address where we should send the file;
     */
    public InetAddress findIp(String fileName) {
        //TODO
        return null;
    }

    /**
     * This helper function determines if we are the owner of the file.
     * this means we should at our file to our log
     * @param fileName the name of the file we need to determine ownership off.
     * @return true if we are the owner.
     */
    public boolean isOwner(String fileName) {
        //TODO
        return false;
    }
}
