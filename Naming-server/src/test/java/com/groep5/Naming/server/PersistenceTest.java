package com.groep5.Naming.server;

import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class PersistenceTest {

    @Test
    public void testWrite() {
        try {
            HashMap<Integer, InetAddress> map = new HashMap<Integer, InetAddress>();
            map.put(16, Inet4Address.getByName("127.0.0.1"));
            Persistence.SaveMap(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
