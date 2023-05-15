package com.groep5.Node;

import com.groep5.Node.Service.Unicast.Receivers.FileReceiver;
import com.groep5.Node.Service.Unicast.Senders.FileSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Logger;

public class FileTransferTest {

    @Test
    public void testFileTransfer() {
        try {
            String testLine = "Hello World!";
            File file = new File("test.file");
            PrintWriter writer = new PrintWriter(file);
            writer.println(testLine);
            writer.flush();
            writer.close();

            FileReceiverTest fileReceiver = new FileReceiverTest();
            fileReceiver.start();

            new FileSender(file, (Inet4Address) Inet4Address.getLocalHost()).start();
            fileReceiver.join();
            File receivedFile = fileReceiver.receivedFile;

            Assertions.assertNotNull(receivedFile);

            BufferedReader reader = new BufferedReader(new FileReader(receivedFile));
            Assertions.assertEquals(testLine, reader.readLine());


            Assertions.assertTrue(file.delete());
            Assertions.assertTrue(receivedFile.delete());

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            throw new RuntimeException(e);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    static class FileReceiverTest extends Thread {
        private final Logger logger = Logger.getLogger(this.getClass().getName());
        public File receivedFile;

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(4321);
                logger.info("Opened socket");

                Socket clientSocket = serverSocket.accept();
                logger.info("Received Connection");

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String[] message = reader.readLine().split(";");
                logger.info("Received message: " + Arrays.toString(message));
                Assertions.assertEquals("replication", message[0]);
                Assertions.assertEquals("test.file", message[1]);
                message[1] = "received.file";

                this.receivedFile = new FileReceiver(message, clientSocket).receive();


                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}



