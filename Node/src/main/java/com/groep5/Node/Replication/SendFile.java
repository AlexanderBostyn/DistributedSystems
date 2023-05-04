package com.groep5.Node.Replication;

import com.groep5.Node.Node;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class SendFile implements Runnable {
    public Node node;
    public File file;
    public int fileHash = -1;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    SendFile(Node node, File file) {
        this.node = node;
        this.file = file;
    }

    public void calcHash() {
        fileHash = node.calculateHash(file.getName());
    }

    public int findNodeHash() {
        Node currentNode = node;



        int current = node.nodeHash;
        int newNode = node.previousHash;
        if(fileHash < current) {
            while(newNode > current)
            do {
                current = newNode;
                newNode = node.previousHash;
            } while (true);
        }
        else if (fileHash > current) {
            do {

            } while (true);
        }
        return 0;
    }

    public void Send() {
        String hostname = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(hostname, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            if (this.file != null) {
                if (file.isFile()) {
                    out.writeObject(file);
                    out.flush();
                    logger.info("Sent file: " + file.getName());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        calcHash();
        findNodeHash();
        Send();
    }
}
