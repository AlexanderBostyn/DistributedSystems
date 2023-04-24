package com.groep5.Node.Unicast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class UnicastSender implements Runnable{
    private Socket socket;
    private int previous;
    private int next;
    public UnicastSender(int previous, int next, String IP) throws IOException {
        this.previous = previous;
        this.next = next;
        socket = new Socket(IP, 4321);
    }

    private void SendMessage() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(previous + ";" + next);
        socket.close();
    }

    @Override
    public void run() {

    }
}
