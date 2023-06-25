package MainPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import Menu.*;

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
    }
}