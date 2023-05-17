package com.groep5.Node;

import com.groep5.Node.Model.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@SpringBootTest
public class LogTest {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Test
    public void InetAddressEqualityTest() {
        try {
            Inet4Address address1 = (Inet4Address) Inet4Address.getByName("google.com");
            Inet4Address address2 = (Inet4Address) Inet4Address.getByAddress(address1.getAddress());
            logger.info("address1: " + address1.toString());
            logger.info("Address2: " + address2.toString());
            Assertions.assertEquals(address1, address2, "Addresses are not equal");
            Inet4Address address3 = (Inet4Address) Inet4Address.getByName("8.8.8.8");
            Set<Inet4Address> set = new HashSet<>(List.of(address1, address2, address3));
            Assertions.assertTrue(set.contains((Inet4Address) Inet4Address.getByName("8.8.8.8")));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void LogContainsTest() throws UnknownHostException {
        Log log = new Log();
        Log.LogEntry entry = null;
        for (int i = 0; i < 5; i++) {
            entry = new Log.LogEntry();
            entry.setFileName("file" + (i + 1) + ".txt");
            for (int j = 0; j < i + 1; j++) {
                entry.add((Inet4Address) Inet4Address.getByName("8.8.8." + j));
            }
            log.put(entry);
        }
        logger.info("Log: " + log.toString());
        Assertions.assertTrue(log.contains("file1.txt"));
        Assertions.assertFalse(log.contains("file.txt"));
        Assertions.assertNull(log.get("file.txt"));
        Assertions.assertNotNull(log.get("file1.txt"));
        Assertions.assertEquals("file1.txt",log.get("file1.txt").getFileName() );
        Assertions.assertTrue(log.get("file4.txt").contains((Inet4Address) Inet4Address.getByName("8.8.8.3")));
        Assertions.assertTrue(log.delete("file1.txt"));
        Assertions.assertFalse(log.delete("file.txt"));
        Assertions.assertTrue(log.delete("file2.txt", (Inet4Address) Inet4Address.getByName("8.8.8.0")));
        Assertions.assertFalse(log.delete("file2.txt", (Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertFalse(log.delete("file.txt", (Inet4Address) Inet4Address.getByName("8.8.8.0")));
        Assertions.assertFalse(log.delete("file.txt", (Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertFalse(log.add("file3.txt"));
        Assertions.assertTrue(log.add("file0.txt"));
        Assertions.assertTrue(log.get("file0.txt").getAddresses().isEmpty());
        Assertions.assertFalse(log.add("file15.txt", (Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertTrue(log.get("file15.txt").contains ((Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertTrue(log.add("file0.txt", (Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertTrue(log.get("file0.txt").contains((Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertEquals(1, log.get("file0.txt").getAddresses().size());
    }

}
