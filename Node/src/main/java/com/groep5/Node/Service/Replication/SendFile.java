package com.groep5.Node.Service.Replication;

import com.groep5.Node.Node;
import com.groep5.Node.SpringContext;
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
    public int fileHash;
    String ip = "";
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public SendFile(File file, String ip) {
        this.node=getNode();
        this.file = file;
        this.ip = ip;
    }
    public SendFile(File file) {
        this.node = getNode();
        this.file = file;
    }
    private Node getNode() {
        return SpringContext.getBean(Node.class);
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
        this.node.addLog(file, result);
        return result;
    }

    public void Send() throws UnknownHostException {
        if (ip.equals(""))
        {
            calcHash();
            ip = findNodeHash();
        }

        try {
            Socket socket = new Socket(ip, 4321);
            if (this.file != null) {
                if (file.isFile()) {
                    logger.info("Sending file: " + file.getName() + ", to: " + ip);
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println("replication;" + file.getName() + ";" + file.length());
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

                    logger.info("Finished sending file: " + file.getName());
                }
            }
            this.node.addLog(file, ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Send();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
