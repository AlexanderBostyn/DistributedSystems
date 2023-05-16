package com.groep5.Node.Service.Unicast.Senders;

import com.groep5.Node.Model.Node;
import com.groep5.Node.SpringContext;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * This Class sends a given file to a given destination.
 * This class should not be responsible for figuring out where a file should be sent.
 * message starts with: "replication;filename;filelength"
 */
public class FileSender extends Thread {
    private final File file;
    private final Inet4Address destination;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public FileSender(File file, Inet4Address destination) {
        this.file = file;
        this.destination = destination;
    }

    private Node getNode() {
        return SpringContext.getBean(Node.class);
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(destination, 4321);
            if (this.file != null) {
                if (file.isFile()) {
                    logger.info("Sending file: " + file.getName() + ", to: " + destination);
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println("replication;" + file.getName() + ";" + file.length());
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

                    logger.info("Finished sending file: " + file.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
