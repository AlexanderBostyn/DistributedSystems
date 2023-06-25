package Home.Files;

import Data.DataContainer;
import Home.HomeMain;
import Nodes.NodeMain;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class FilesCardPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardContainer;
    public FilesCardPanel() throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height/2 - 50);

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(new Color(0, 12, 28));
        cardContainer.setOpaque(true);

        DataContainer dataContainer = new DataContainer();
        String files = dataContainer.getFiles();
        int amountOfPanels;
        String[] strings;
        if(files.contains(":")) {
            strings = files.split(":");
        }
        else {
            strings = new String[]{files};
        }
        double length = strings.length;
        amountOfPanels = (int) Math.ceil(length/8);
        if (amountOfPanels == 0) {
            amountOfPanels = 1;
        }

        int filesLeft = (int) length;

        int startPositie = 0;
        for(int i = 0;i<amountOfPanels;i++) {
            createFilePanel(startPositie, strings, filesLeft);
            startPositie += 8;
            filesLeft -= 8;
        }

        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);
    }

    public void createFilePanel(int startPositie, String[] files, int filesLeft) {
        FilesList filesList = new FilesList(this, startPositie, files, filesLeft);
        cardContainer.add(filesList);
    }

    public void showNextPanel() {
        cardLayout.next(cardContainer);
    }

    public void showPreviousPanel() {
        cardLayout.previous(cardContainer);
    }
}