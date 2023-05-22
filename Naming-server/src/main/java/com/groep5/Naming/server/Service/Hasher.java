package com.groep5.Naming.server.Service;

import com.groep5.Naming.server.Model.Node;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface Hasher {
    public InetAddress locateFileByName(String name);
    public InetAddress locateFileById(int id);
    public int addNode(String name, String strAddress) throws UnknownHostException;
    public void deleteNode(int id);

    public int calcHashId(String name);

    public int returnAmountOfNodes();
    public void  deleteNodeByAddress(InetAddress nodeAddress);

    public int previousId(int id);

    public InetAddress locateNodeById(int id);

    public int nextId(int id);
}
