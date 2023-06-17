package Files;

import Data.DataContainer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FilesCardPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardContainer;

    public FilesCardPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 50, screenSize.width, screenSize.height);

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(new Color(0, 12, 28));
        cardContainer.setOpaque(true);

        DataContainer dataContainer = new DataContainer();
        ArrayList<String> fileList = dataContainer.getFiles();
        double length = fileList.size();
        int amountOfPanels = (int) Math.ceil(length/11);
        int filesLeft = (int) length;

        int startPositie = 0;
        for(int i = 0;i<amountOfPanels;i++) {
            createFilePanel(startPositie, fileList, filesLeft);
            startPositie += 11;
            filesLeft -= 11;
        }

        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);
    }

    public void createFilePanel(int startPositie, ArrayList<String> fileList, int filesLeft) {
        FilesList filesList = new FilesList(this, startPositie, fileList, filesLeft);
        cardContainer.add(filesList);
    }

    public void showNextPanel() {
        cardLayout.next(cardContainer);
    }

    public void showPreviousPanel() {
        cardLayout.previous(cardContainer);
    }
}

