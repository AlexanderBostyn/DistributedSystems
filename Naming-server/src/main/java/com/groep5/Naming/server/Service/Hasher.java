package com.groep5.Naming.server.Service;

import java.net.InetAddress;

public interface Hasher {
    public InetAddress locateFileByName(String name);
    public InetAddress locateFileById(int id);
    public int addNode(String name, String strAddress);
    public int addFile(String fileName);
    public void deleteNode(String name, String strAddress);

    public int calcHashId(String name);
    


}
