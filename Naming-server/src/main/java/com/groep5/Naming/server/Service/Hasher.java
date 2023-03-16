package com.groep5.Naming.server.Service;

import java.net.InetAddress;

public interface Hasher {
    public InetAddress locateFileByName(String name);
    public InetAddress locateFileById(String name);
    public int addNode(String strAddress);
    public int addFile(String fileName);
    public void deleteNode(String strAddress);
    


}
