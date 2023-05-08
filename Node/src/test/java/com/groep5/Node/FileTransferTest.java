package com.groep5.Node;

import com.groep5.Node.Replication.StartUp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class FileTransferTest {

    @Test
    public void testFileTransfer() {
        FileReceiver fileReceiver = new FileReceiver();
        fileReceiver.start();
        FileSender fileSender = new FileSender();
        fileSender.start();

    }
}

class FileReceiver extends Thread {
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(54321);
            Socket client = ss.accept();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String[] message = bufferedReader.readLine().split(";");
            System.out.println(Arrays.toString(message));
//            bufferedReader.close();
            String filename = message[1];
            long size = Long.parseLong(message[2]);
            File file = new File("src/main/resources/replicated/" + filename);
            int bytes = 0;
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                DataInputStream dis = new DataInputStream(client.getInputStream());
                byte[] buffer = new byte[4 * 1024];
                while (size > 0 && (bytes = dis.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    fileOutputStream.write(buffer, 0, bytes);
                    size -= bytes;
                }
                dis.close();
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

class FileSender extends Thread {

    @Override
    public void run() {
        File file = new File("src/main/resources/local/test.file");
        Socket socket = null;
        try {
            socket = new Socket("localhost", 54321);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("replication;test.file;" + file.length());
            if (file.isFile()) {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buf = new byte[4 * 1024];
                int bytes = 0;
                while ((bytes = fileInputStream.read(buf)) != -1) {
                    dataOutputStream.write(buf, 0, bytes);
                    dataOutputStream.flush();
                }
                fileInputStream.close();
                dataOutputStream.close();

                socket.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

