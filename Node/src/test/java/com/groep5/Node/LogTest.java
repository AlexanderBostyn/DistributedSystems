package com.groep5.Node;

import com.groep5.Node.Model.Log;
import com.groep5.Node.Service.Unicast.Receivers.LogReceiver;
import com.groep5.Node.Service.Unicast.Senders.LogSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
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
        Log log = createLog();
        logger.info("Log: " + log);
        Assertions.assertEquals(5, log.size());
        Assertions.assertTrue(log.contains("file1.txt"));
        Assertions.assertFalse(log.contains("file.txt"));

    }

    @Test
    public void LogGetTest() throws UnknownHostException {
        Log log = createLog();
        Assertions.assertNull(log.get("file.txt"));
        Assertions.assertNotNull(log.get("file1.txt"));
        Assertions.assertEquals(4, log.get("file4.txt").size());
        Assertions.assertEquals("file1.txt",log.get("file1.txt").getFileName() );
        Assertions.assertTrue(log.get("file4.txt").contains((Inet4Address) Inet4Address.getByName("8.8.8.3")));
    }

    @Test
    public void LogDeleteTest() throws UnknownHostException {
        Log log = createLog();
        Assertions.assertTrue(log.delete("file1.txt"));
        Assertions.assertFalse(log.delete("file.txt"));
        Assertions.assertTrue(log.delete("file2.txt", (Inet4Address) Inet4Address.getByName("8.8.8.0")));
        Assertions.assertFalse(log.delete("file2.txt", (Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertFalse(log.delete("file.txt", (Inet4Address) Inet4Address.getByName("8.8.8.0")));
        Assertions.assertFalse(log.delete("file.txt", (Inet4Address) Inet4Address.getByName("8.8.8.8")));
    }

    @Test
    public void LogAddTest() throws UnknownHostException {
        Log log = createLog();
        Assertions.assertFalse(log.add("file3.txt"));
        Assertions.assertTrue(log.add("file0.txt"));
        Assertions.assertTrue(log.get("file0.txt").getAddresses().isEmpty());
        Assertions.assertFalse(log.add("file15.txt", (Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertTrue(log.get("file15.txt").contains ((Inet4Address) Inet4Address.getByName("8.8.8.8")));

        Assertions.assertTrue(log.add("file0.txt", (Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertTrue(log.get("file0.txt").contains((Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertEquals(1, log.get("file0.txt").getAddresses().size());

        Log.LogEntry entry = new Log.LogEntry();
        entry.add((Inet4Address) Inet4Address.getByName("8.8.8.0"));
        entry.setFileName("file1.txt");
        Assertions.assertEquals(entry, log.get("file1.txt"));
        Assertions.assertFalse(log.add(entry));
        entry.setFileName("file2.txt");
        Assertions.assertFalse(log.add(entry));
        entry.add((Inet4Address) Inet4Address.getByName("8.8.8.8"));
        Assertions.assertTrue(log.add(entry));
        Assertions.assertTrue(log.get("file2.txt").contains((Inet4Address) Inet4Address.getByName("8.8.8.8")));
        Assertions.assertTrue(log.get("file2.txt").contains((Inet4Address) Inet4Address.getByName("8.8.8.0")));
        Assertions.assertTrue(log.get("file2.txt").contains((Inet4Address) Inet4Address.getByName("8.8.8.1")));

    }

    @Test
    public void logSendingTest() {
        try {
            Log log = createLog();
            TestLogReceiver logReceiver = new TestLogReceiver();
            logReceiver.start();
            new LogSender(log, (Inet4Address) Inet4Address.getLocalHost()).start();

            //wait till logReceiver is finished
            logReceiver.join();

            Assertions.assertEquals(log, logReceiver.log, "Logs aren't equal");
            System.out.println(log);
            System.out.println(logReceiver.log);
        } catch (UnknownHostException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    static class TestLogReceiver extends Thread {
        private final Logger logger = Logger.getLogger(this.getClass().getName());
        public Log log;

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(4321);
                logger.info("opened socket");
                Socket clientSocket = serverSocket.accept();
                logger.info("Connection received");
                String[] message = "log;3".split(";");
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                message= reader.readLine().split(";");
                this.log = new LogReceiver(message, clientSocket).receive();
                logger.info("Log received and processed");
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Log createLog() throws UnknownHostException {
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
        return log;
    }

}
