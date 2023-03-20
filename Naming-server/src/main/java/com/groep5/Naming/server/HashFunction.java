package com.groep5.Naming.server;

import java.io.File;
import java.net.InetAddress;
import java.util.TreeMap;

public class HashFunction {

    private final TreeMap<Integer, String> nodeMap;
    private final TreeMap<Integer, File> fileMap;
    private static HashFunction instance = null;

    private HashFunction() {
        this.nodeMap = new TreeMap<Integer, String>();
        this.fileMap = new TreeMap<Integer, File>();
    }
    public static HashFunction getInstance() {
        if(instance == null) {
            instance = new HashFunction();
        }
        return instance;
    }

    public int getNodeHash(String name) {
        double max = 2147483647;
        double hash;
        do {
            hash = (name.hashCode() + max) / max * (32768/2);
        } while(nodeMap.containsKey((int) hash) && (nodeMap.get((int) hash) != name));
        return (int) hash;
    }

    public void addNode(String name) {
        int hash = getNodeHash(name);
        nodeMap.put(hash,name);
    }

    public void removeNode(int id) {
        nodeMap.remove(id);
    }

    public int getFileHash(File f) {
        double max = 2147483647;
        double hash;
        do {
            hash = (f.hashCode() + max) / max * (32768/2);
        } while(fileMap.containsKey((int) hash) && (fileMap.get((int) hash) != f));
        return (int) hash;
    }

    public void addFile(File f) {
        int hash = getFileHash(f);
        fileMap.put(hash,f);
    }

    public void removeFile(int id) {
        fileMap.remove(id);
    }

    public static void main(String[] args) {
        HashFunction hashFunction = HashFunction.getInstance();
        //InetAddress address = InetAddress.getByAddress("127.0.0.1");
        File file1 = new File("\"C:\\UAProgrammas\\IntellijProjects\\DIST\\Lab2\"");
        System.out.println(hashFunction.getFileHash(file1));
        File file2 = new File("\"C:\\UAProgrammas\\IntellijProjects\\DIST\\Lab1\"");
        System.out.println(hashFunction.getFileHash(file2));
    }
}
