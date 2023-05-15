package com.groep5.Node;

import com.groep5.Node.Service.Unicast.Receivers.LogReceiver;
import com.groep5.Node.Service.Unicast.Senders.LogSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class LogTransferTest {
    @Test
    public void logSendingTest() {
        try {
            HashMap<File, ArrayList<Inet4Address>> log = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                ArrayList<Inet4Address> value = new ArrayList<>(List.of((Inet4Address) Inet4Address.getLocalHost()));
                File key = new File(Integer.toString(i));
                log.put(key, value);
            }

            LogReceiverTest logReceiver = new LogReceiverTest();
            logReceiver.start();
            new LogSender(log, (Inet4Address) Inet4Address.getLocalHost()).start();

            //wait till logReceiver is finished
            logReceiver.join();

            Assertions.assertEquals(log, logReceiver.log, "Logs aren't equal");
            System.out.println(log);
            System.out.println(logReceiver.log);
            //delete all created files;
            log.keySet().forEach(file -> file.delete());
        } catch (UnknownHostException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    static class LogReceiverTest extends Thread {
        private final Logger logger = Logger.getLogger(this.getClass().getName());
        public HashMap<File, ArrayList<Inet4Address>> log;

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
}

