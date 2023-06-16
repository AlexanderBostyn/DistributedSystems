package Files;

import javax.swing.*;
import java.awt.*;

public class FilesList extends JPanel {
    public FilesCardPanel filesCardPanel;
    public int numRows;
    public FilesList(FilesCardPanel fcp, int j) {
        filesCardPanel = fcp;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height);
        setBackground(new Color(0, 12, 28));
        setOpaque(false);

        setLayout(new GridLayout(17,1, 0,5));

        FilesCellFactory fcf = new FilesCellFactory();

        add(fcf.createFirstCell());

        numRows = 1;
        /*while(numRows < 10)*/ for(int i = 0; i<5;i++) {
            if (numRows == 12) {
                add(fcf.createBtnCell(fcp));
                //filesCardPanel.createFilePanel();
                break;
            }
            else {
                add(fcf.createCell(String.valueOf(j), "Portfolio_DIST.pdf", "node3.6dist", "node2.6dist", "Unlocked"));
                numRows++;
            }
        }

        if (numRows < 12) {
            while (numRows < 12) {
                add(fcf.createEmptyCell());
                numRows++;
            }
            add(fcf.createBtnCell(fcp));
        }
    }
}
