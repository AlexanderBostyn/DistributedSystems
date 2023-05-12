package com.groep5.Node.Service.Unicast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

//TODO kan verwijderst worden atm
//zit momenteel in Node
public class UnicastSender {
    private final Socket socket;
    private final int previous;
    private final int next;
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
}
