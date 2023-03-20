package com.groep5.Naming.server.Service;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DummyHasher implements Hasher {

    @Override
    public InetAddress locateFileByName(String name) {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InetAddress locateFileById(int id) {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int addNode(String name, String strAddress) {
        return 123;
    }

    @Override
    public int addFile(String fileName) {
        return 1234;
    }

    @Override
    public void deleteNode(String name, String strAddress) {

    }

    @Override
    public int calcHashId(String name) {
        return name.length();
    }
}
