package MainPanel;

import javax.swing.*;
import java.awt.*;
import Menu.*;

public class MainFrame extends JFrame{
    public MainFrame() {
        this.setTitle("Project Distributed Systems");
        this.setIconImage(new ImageIcon("src/Images/UA.png").getImage());

        this.getContentPane().setBackground(new Color(255, 255, 255, 255));
        this.setLayout(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//make sure the progam closes when you press x
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.setResizable(true);//you cannot manually resize the program
        this.setSize(500, 397);//panels nemen blijkbaar 37 extra pixels in in beide dimensies dan ingevuld?
        this.setLayout(null);

        this.add(new TitleLabel());
        this.add(new MenuPanel());
        //TODO: add more panels/labels with all the functionality
        //TODO: Adding nodes
        //TODO: Removing nodes
        //TODO: Showing the list of all nodes
        //TODO: Showing the list of all files on the selected node (local && replicated in two groups)
        //TODO: Showing the configuration data of the selected node (i.e., previous and next ID)
        //TODO: All the other things you may want to add are bonus!

        this.setVisible(true);
    }
}