package com.groep5.Node.Replication;

import com.groep5.Node.Node;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
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

    public String findNodeHash() throws UnknownHostException {
        String result = WebClient.create("http://" + node.namingServerAddress.getHostAddress() + ":54321")
                .get()
                .uri("/file/" + fileHash)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return result;
    }

    public void Send(String ip) {
        String hostname = ip;
        int port = 4321;

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
        try {
            String ip = findNodeHash();
            Send(ip);
        } catch (UnknownHostException e) {
            logger.severe("Error with sending file");
            throw new RuntimeException(e);
        }
    }
}
