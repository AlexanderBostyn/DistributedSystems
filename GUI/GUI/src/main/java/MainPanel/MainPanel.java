package MainPanel;

import Files.FilesMain;
import Home.HomeMain;
import Nodes.NodeMain;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardContainer;
    public MainPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(200, 75, screenSize.width-200, screenSize.height-75);
        setOpaque(false);

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);

        JPanel homePanel = new HomeMain();
        JPanel nodePanel = new NodeMain(this);
        JPanel filesPanel = new FilesMain();
        JLabel label1 = new JLabel("HomeMain");
        JLabel label2 = new JLabel("NodeMain");
        JLabel label3 = new JLabel("FilesMain");
        homePanel.add(label1);
        nodePanel.add(label2);
        filesPanel.add(label3);

        cardContainer.add(homePanel, "HomeMain");
        cardContainer.add(nodePanel, "NodeMain");
        cardContainer.add(filesPanel, "FilesMain");

        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);
    }

    public void showPanel(int i) {
        if (i == 0) {
            cardLayout.show(cardContainer, "HomeMain");
        } else if (i == 1) {
            cardLayout.show(cardContainer, "NodeMain");
        } else if (i == 2){
            cardLayout.show(cardContainer, "FilesMain");
        }
    }
}
