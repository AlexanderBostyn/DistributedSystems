package com.groep5.Naming.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;


public class FileOwnerTest {

    @Test
    public void testFilter() {
        Map<Integer, InetAddress> nodeMap = new TreeMap<>();
        try {
            nodeMap.put(5, Inet4Address.getByName("192.168.0.5"));
            nodeMap.put(12, Inet4Address.getByName("192.168.0.12"));
            nodeMap.put(17, Inet4Address.getByName("192.168.0.17"));
            nodeMap.put(28, Inet4Address.getByName("192.168.0.28"));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("/192.168.0.17", locateFileById(18, nodeMap).toString());
        Assertions.assertEquals("/192.168.0.28", locateFileById(4, nodeMap).toString());

    }

    private InetAddress locateFileById(int id, Map<Integer, InetAddress> nodeMap) {
        return nodeMap.get(nodeMap.keySet().stream()
                .filter(integer -> integer < id)
                .max(Integer::compareTo)
                .orElseGet(
                        () -> nodeMap.keySet().stream().max(Integer::compareTo).get()
                )
        );
    }
}
