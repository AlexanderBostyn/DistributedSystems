package com.groep5.Naming.server;

import com.google.common.hash.Hashing;
import com.groep5.Naming.server.Service.Hasher;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

public class HashFunction implements Hasher {

    private final TreeMap<Integer, InetAddress> nodeMap;
    private File file;
    private ApplicationContext context;

    public HashFunction(ApplicationContext context) {
        this.context = context;
        this.file = context.getBean("dataFile", File.class);
        nodeMap = Persistence.LoadMap(file);
    }

    @Override
    public int calcHashId(String name) {
        double max = 2147483647;
        return (int) ((Hashing.sha256().hashString(name, StandardCharsets.UTF_8).hashCode() + max) / max * (32768/2));
    }

    @Override
    public void deleteNode(String hostName) {
        nodeMap.remove(calcHashId(hostName));
        Persistence.SaveMap(nodeMap, file.getName());
    }

    @Override
    public int addNode(String name, String strAddress) throws UnknownHostException {
        nodeMap.put(calcHashId(name), Inet4Address.getByName(strAddress));
        Persistence.SaveMap(nodeMap, file.getName());
        return calcHashId(name);
    }
    @Override
    public InetAddress locateFileByName(String name) {
        //TODO
        return null;
    }

    @Override
    public InetAddress locateFileById(int id) {

        //TODO
        return null;
    }







}
