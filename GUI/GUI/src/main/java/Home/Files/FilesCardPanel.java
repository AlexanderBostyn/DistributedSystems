package Home.Files;

import Files.FilesMain;
import Home.HomeMain;
import Nodes.NodeMain;

import javax.swing.*;
import java.awt.*;

public class FilesCardPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardContainer;
    public FilesCardPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height/2 - 50);

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(new Color(0, 12, 28));
        cardContainer.setOpaque(true);

        createFilePanel();
        createFilePanel();
        createFilePanel();

        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);
    }
    int i = 0;
    public void createFilePanel() {
        FilesList filesList = new FilesList(this, i++);
        cardContainer.add(filesList);
    }

    public void showNextPanel() {
        cardLayout.next(cardContainer);
    }

    public void showPreviousPanel() {
        cardLayout.previous(cardContainer);
    }
}
