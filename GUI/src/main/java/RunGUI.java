import MainPanel.MainFrame;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class RunGUI {
    public static void main(String args[]) throws IOException  //static method
    {
        MainFrame mf = new MainFrame();
        mf.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
