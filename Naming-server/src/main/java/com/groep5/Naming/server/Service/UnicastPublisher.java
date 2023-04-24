package com.groep5.Naming.server.Service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Service
public class UnicastPublisher {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void sendMessage(String message, String address) {
        try {
            Socket socket = new Socket(address, 4321); // replace 12345 with the desired destination port
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(message.getBytes());
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error sending TCP message: " + e.getMessage());
        }
    }
}
