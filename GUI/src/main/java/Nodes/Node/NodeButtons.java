package Nodes.Node;

import MainPanel.MainPanel;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class NodeButtons extends JPanel {
    public MainPanel mainPanel;
    public ArrayList<Boolean> status;
    public Logger log = Logger.getLogger(this.getClass().getName());
    public NodeButtons(MainPanel mp, int i, ArrayList<Boolean> status) {
        this.status = status;
        mainPanel = mp;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(10, 2*screenSize.height/9 + 50, (screenSize.width - 450)/4 - 20, screenSize.height/9 - 60);
        setBackground(new Color(0, 15, 30));
        setLayout(new GridLayout(1,2));

        JButton files = new JButton("Files");
        files.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.showPanel(1+i);
            }
        });
        JButton switchbtn = new JButton("Turn on/off");
        switchbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(status.get(i-1)) {
                    JOptionPane.showMessageDialog(null,"Node shutdown","Node Shutdown",JOptionPane.INFORMATION_MESSAGE);
                    status.set(i-1,false);
                    int socket = 8080 + i;
                    try {
                        WebClient.create("http://" + InetAddress.getByName("localhost").getHostAddress() + ":" + socket)
                                .put()
                                .uri("/shutdown");
                    } catch (UnknownHostException ex) {
                        throw new RuntimeException(ex);
                    } catch (WebClientResponseException ignore) {
                        log.info("failed to shutdown node");
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Node Startup","Node Startup",JOptionPane.INFORMATION_MESSAGE);
                    status.set(i-1,true);
                    int socket = 8080 + i;
                    try {
                        WebClient.create("http://" + InetAddress.getByName("localhost").getHostAddress() + ":" + socket)
                                .put()
                                .uri("/activate");
                    } catch (UnknownHostException ex) {
                        throw new RuntimeException(ex);
                    } catch (WebClientResponseException ignore) {
                        log.info("failed to activate node");
                    }
                }
                try {
                    mainPanel.refresh(status);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                mainPanel.showPanel(1);
            }
        });

        add(files);
        add(switchbtn);
    }
}
