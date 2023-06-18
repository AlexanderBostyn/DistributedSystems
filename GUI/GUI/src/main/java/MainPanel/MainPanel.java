package MainPanel;

import Files.FilesMain;
import Home.HomeMain;
import Nodes.NodeMain;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardContainer;
    public ArrayList<Boolean> status;
    public MainPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(200, 75, screenSize.width-200, screenSize.height-75);
        setOpaque(false);

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);

        JPanel homePanel = new HomeMain();
        status = new ArrayList<>();
        for(int i=0;i<5;i++) {
            status.add(false);
        }
        JPanel nodePanel = new NodeMain(this, status);
        JLabel label1 = new JLabel("HomeMain");
        JLabel label2 = new JLabel("NodeMain");
        homePanel.add(label1);
        nodePanel.add(label2);
        cardContainer.add(homePanel, "HomeMain");
        cardContainer.add(nodePanel, "NodeMain");

        for(int i = 1;i<5;i++) {
            JPanel filesPanel = new FilesMain(i);
            JLabel label = new JLabel("FilesMain" + i);
            filesPanel.add(label);
            cardContainer.add(filesPanel, "FilesMain" + i);
        }

        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);
    }

    public void showPanel(int i) {
        if (i == 0) {
            cardLayout.show(cardContainer, "HomeMain");
        } else if (i == 1) {
            cardLayout.show(cardContainer, "NodeMain");
        } else if (i == 2){
            cardLayout.show(cardContainer, "FilesMain1");
        } else if (i == 3){
            cardLayout.show(cardContainer, "FilesMain2");
        } else if (i == 4){
            cardLayout.show(cardContainer, "FilesMain3");
        } else if (i == 5){
            cardLayout.show(cardContainer, "FilesMain4");
        }
    }

    public void refresh(ArrayList<Boolean> status) {
        this.status = status;
        for (int i=0; i<5;i++) {
            cardContainer.remove(0);
        }

        JPanel homePanel = new HomeMain();
        JPanel nodePanel = new NodeMain(this, status);
        JLabel label1 = new JLabel("HomeMain");
        JLabel label2 = new JLabel("NodeMain");
        homePanel.add(label1);
        nodePanel.add(label2);
        cardContainer.add(homePanel, "HomeMain");
        cardContainer.add(nodePanel, "NodeMain");

        for(int i = 1;i<5;i++) {
            JPanel filesPanel = new FilesMain(i);
            JLabel label = new JLabel("FilesMain" + i);
            filesPanel.add(label);
            cardContainer.add(filesPanel, "FilesMain" + i);
        }
    }
}
