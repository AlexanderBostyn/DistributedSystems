import MainPanel.MainFrame;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class RunGUI {
    public static void main(String args[]) throws IOException  //static method
    {
        System.out.println("test");
        //ServerSocket socket = new ServerSocket(54321);
        //String address = String.valueOf(socket.getInetAddress());
        String address= InetAddress.getByName("localhost").getHostAddress();
        System.out.println(address);
        String test =  WebClient.create("http://" + address + ":54321")
                .get()
                .uri("/status")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(test);
        MainFrame mf = new MainFrame();
        mf.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
