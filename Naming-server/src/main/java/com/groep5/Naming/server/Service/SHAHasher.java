package com.groep5.Naming.server.Service;

import com.google.common.hash.Hashing;
import com.groep5.Naming.server.Persistence;
import com.groep5.Naming.server.Service.Hasher;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;


public class SHAHasher implements Hasher {

    private final TreeMap<Integer, InetAddress> nodeMap;
    private File file;
    private ApplicationContext context;

    public SHAHasher(ApplicationContext context) {
        this.context = context;
        this.file = context.getBean("dataFile", File.class);
        nodeMap = Persistence.LoadMap(file);
    }

    @Override
    public int calcHashId(String name) {
        double max = 2147483647;
        return (int) ((Hashing.sha256().hashString(name, StandardCharsets.UTF_8).hashCode() + max) / max * (32768 / 2));
    }

    @Override
    public int returnAmountOfNodes() {

        return nodeMap.size();
    }

    @Override
    public void deleteNodeByAddress(InetAddress nodeAddress) {
        nodeMap.entrySet().removeIf(integerInetAddressEntry -> integerInetAddressEntry.getValue() == nodeAddress);
        Persistence.SaveMap(nodeMap, file.getName());
    }

    @Override
    public int previousId(int id) {
        List<Integer> ids = nodeMap.keySet().stream().toList(); //gets keys in ascending order
        int index = ids.indexOf(id);
        if (index == 0) {
            return ids.get(ids.size() - 1);
        }
        return ids.get(index - 1);
    }

    @Override
    public int nextId(int id) {
        List<Integer> ids = nodeMap.keySet().stream().toList();
        int index = ids.indexOf(id);
        if (index == ids.size() -1) {
            return ids.get(0);
        }
        return ids.get(index + 1);
    }
    @Override
    public InetAddress locateNodeById(int id) {
        return nodeMap.get(id);
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
        return locateFileById(calcHashId(name));
    }

    @Override
    public InetAddress locateFileById(int id) {
        return nodeMap.get(nodeMap.keySet().stream()
                .filter(integer -> integer < id)
                .max(Integer::compareTo)
                .orElseGet(
                        () -> nodeMap.keySet().stream().max(Integer::compareTo).get()
                )
        );
    }


}
