package MainPanel;

import Files.FilesMain;
import Home.HomeMain;
import Nodes.NodeMain;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardContainer;
    public ArrayList<Boolean> status;
    public MainPanel() throws IOException {
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
        switch (i) {
            case 0:
                cardLayout.show(cardContainer, "HomeMain");
                break;
            case 1:
                cardLayout.show(cardContainer, "NodeMain");
                break;
            case 2:
                cardLayout.show(cardContainer, "FilesMain1");
                break;
            case 3:
                cardLayout.show(cardContainer, "FilesMain2");
                break;
            case 4:
                cardLayout.show(cardContainer, "FilesMain3");
                break;
            case 5:
                cardLayout.show(cardContainer, "FilesMain4");
                break;
        }
    }

    public void refresh(ArrayList<Boolean> status) throws IOException {
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
