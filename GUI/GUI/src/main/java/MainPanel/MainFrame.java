package MainPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import Home.Files.FilesMain;
import Home.HomeMain;
import Home.NameServer.NameServerMain;
import Home.Nodes.NodesMain;
import Menu.*;
import Nodes.NodeMain;

public class MainFrame extends JFrame{
    public MainPanel mainPanel;
    public MainFrame() throws IOException {
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Project Distributed Systems");
        setIconImage(new ImageIcon("src/Images/UA.png").getImage());
        getContentPane().setBackground(new Color(0, 20, 75));

        setLayout(null);

        mainPanel = new MainPanel();
        MenuPanel menuPanel = new MenuPanel(mainPanel);


        add(new TitleLabel());
        add(new ImageLabel());
        add(menuPanel);
        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        //TODO: add more panels/labels with all the functionality
        //TODO: Adding nodes
        //TODO: Removing nodes
        //TODO: Showing the list of all nodes
        //TODO: Showing the list of all files on the selected node (local && replicated in two groups)
        //TODO: Showing the configuration data of the selected node (i.e., previous and next ID)
        //TODO: All the other things you may want to add are bonus!
    }
}