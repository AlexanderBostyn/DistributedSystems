package com.groep5.Naming.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PersistenceTest {
    private File file;
    private TreeMap<Integer, InetAddress> map = new TreeMap<>();
    @Test
    public void test() {
        try {
            map.put(16, Inet4Address.getByName("127.0.0.1"));
            this.file = Persistence.SaveMap(map, "data.json");
            TreeMap<Integer, InetAddress> loadedMap = Persistence.LoadMap(file);
            System.out.println(map.get(16));
            System.out.println(loadedMap.get(16));
            Assertions.assertEquals(map, loadedMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
