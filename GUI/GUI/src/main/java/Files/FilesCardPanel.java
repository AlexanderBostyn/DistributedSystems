package Files;

import Data.DataContainer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FilesCardPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardContainer;
    public FilesCardPanel(int j) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(50,100,screenSize.width-300, screenSize.height-300);

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(new Color(0, 12, 28));
        cardContainer.setOpaque(true);

        DataContainer dataContainer = new DataContainer();
        ArrayList<String> fileList = dataContainer.getFiles();
        ArrayList<String> newFileList = new ArrayList<>();
        for(String s : fileList) {
            String[] strings = s.split(";");
            if(strings[3].equals("node" + j + ".6dist")) {
                newFileList.add(s);
            }
        }

        double length = newFileList.size();
        int amountOfPanels = (int) Math.ceil(length/12);
        if(amountOfPanels == 0) {
            amountOfPanels = 1;
        }
        int filesLeft = (int) length;

        int startPositie = 0;
        for(int i = 0;i<amountOfPanels;i++) {
            createFilePanel(startPositie, newFileList, filesLeft);
            startPositie += 12;
            filesLeft -= 12;
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
