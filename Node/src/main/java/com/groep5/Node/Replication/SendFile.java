package com.groep5.Node.Replication;

import com.groep5.Node.Node;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class SendFile extends Thread {
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
        this.node.log.add(result);
        return result;
    }

    public void Send(String ip) {
        String hostname = ip;
        int port = 4321;

        try {
            Socket socket = new Socket(hostname, port);
            if (this.file != null) {
                if (file.isFile()) {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buf = new byte[4*1024];
                    int bytes = 0;
                    while((bytes = fileInputStream.read(buf)) != -1) {
                        dataOutputStream.write(buf,0,bytes);
                        dataOutputStream.flush();
                    }
                    fileInputStream.close();
                    dataOutputStream.close();

                    socket.close();

                    this.node.addLog(ip);

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
