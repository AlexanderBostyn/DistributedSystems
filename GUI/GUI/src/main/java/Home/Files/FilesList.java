package Home.Files;

import Home.Nodes.NodeCellFactory;

import javax.swing.*;
import java.awt.*;

public class FilesList extends JPanel {
    public FilesCardPanel filesCardPanel;
    public int numRows;
    public FilesList(FilesCardPanel fcp, int j) {
        filesCardPanel = fcp;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height/2 - 50);
        setOpaque(false);

        setLayout(new GridLayout(10,1, 0,5));

        FilesCellFactory fcf = new FilesCellFactory();

        add(fcf.createFirstCell());

        numRows = 1;
        /*while(numRows < 10)*/ for(int i = 0; i<5;i++) {
            if (numRows == 9) {
                add(fcf.createBtnCell(fcp));
                //filesCardPanel.createFilePanel();
                break;
            }
            else {
                add(fcf.createCell(String.valueOf(j), "Portfolio_DIST.pdf", "node3.6dist", "node2.6dist", "Unlocked"));
                numRows++;
            }
        }

        if (numRows < 9) {
            while (numRows < 9) {
                add(fcf.createEmptyCell());
                numRows++;
            }
            add(fcf.createBtnCell(fcp));
        }
    }
}
