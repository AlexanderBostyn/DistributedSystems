package Menu;

import Home.HomeMain;
import Home.Nodes.NodesMain;
import MainPanel.MainFrame;
import MainPanel.MainPanel;
import Nodes.NodeMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuPanel extends JPanel {
    public MainPanel mainPanel;

    public MenuPanel(MainPanel mp) {
        mainPanel = mp;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 75, 200, screenSize.height-75);
        setBackground(new Color(0, 15, 30));

        JButton home = new JButton("Home");
        home.setPreferredSize(new Dimension(100, 50));
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.showPanel(0);
            }
        });
        JButton nodes = new JButton("Nodes");
        nodes.setPreferredSize(new Dimension(100, 50));
        nodes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.showPanel(1);
            }
        });
        JButton refresh = new JButton("Refresh");
        refresh.setPreferredSize(new Dimension(100, 50));
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mainPanel.refresh(mainPanel.status);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                mainPanel.showPanel(0);
            }
        });

        add(home);
        add(nodes);
        add(refresh);
    }
}