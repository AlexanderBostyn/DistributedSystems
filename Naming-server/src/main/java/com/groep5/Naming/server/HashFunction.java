package com.groep5.Naming.server;

import com.google.common.hash.Hashing;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

public class HashFunction {

    private final TreeMap<Integer, String> nodeMap;
    private final TreeMap<Integer, String> fileMap;
    private static HashFunction instance = null;

    private HashFunction() {
        this.nodeMap = new TreeMap<Integer, String>();
        this.fileMap = new TreeMap<Integer, String>();
    }
    public static HashFunction getInstance() {
        if(instance == null) {
            instance = new HashFunction();
        }
        return instance;
    }

    public int getHash(String name) {
        int hash;
        do {
            hash = (Hashing.sha256().hashString(name, StandardCharsets.UTF_8).hashCode());
        } while(nodeMap.containsKey(hash) && (nodeMap.get(hash) != name));
        System.out.println(hash);
        return hash;
    }

    public void addNode(String name) {
        int hash = getHash(name);
        nodeMap.put(hash,name);
    }

    public void removeNode(int id) {
        nodeMap.remove(id);
    }

    public void addFile(String name) {
        int hash = getHash(name);
        fileMap.put(hash,name);
    }

    public void removeFile(int id) {
        fileMap.remove(id);
    }

    /*public static void main(String[] args) {
        HashFunction hashFunction = HashFunction.getInstance();
        String node1 = "testNode1";
        hashFunction.addNode(node1);
        String node2 = "testNode2";
        hashFunction.addNode(node2);
        String node3 = "testNode3";
        hashFunction.addNode(node3);
    }*/
}
