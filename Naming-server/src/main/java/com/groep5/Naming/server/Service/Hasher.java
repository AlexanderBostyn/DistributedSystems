package com.groep5.Naming.server.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface Hasher {
    public InetAddress locateFileByName(String name);
    public InetAddress locateFileById(int id);
    public int addNode(String name, String strAddress) throws UnknownHostException;
    public void deleteNode(String name);

    public int calcHashId(String name);


    void deleteNodeByAddress(InetAddress nodeAddress);
}
