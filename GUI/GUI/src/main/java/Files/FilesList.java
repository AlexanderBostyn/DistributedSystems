package Files;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FilesList extends JPanel {
    public FilesCardPanel filesCardPanel;
    public int numRows = 1;
    public FilesList(FilesCardPanel fcp, int startPositie, ArrayList<String> fileList, int filesLeft) {
        filesCardPanel = fcp;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height);
        setBackground(new Color(0, 12, 28));
        setOpaque(false);

        setLayout(new GridLayout(17,1, 0,5));

        FilesCellFactory fcf = new FilesCellFactory();
        add(fcf.createFirstCell());

        while(numRows < 13) {
            if (numRows == 12) {
                System.out.println(numRows);
                add(fcf.createBtnCell(fcp));
                break;
            }
            else if(filesLeft > 0) {
                String file = fileList.get(startPositie);
                String[] strings = file.split(";");
                add(fcf.createCell(strings[0], strings[1], strings[2], strings[3], strings[4]));
                numRows++;
                startPositie++;
                filesLeft--;
                System.out.println(numRows);
            }
            else if(filesLeft == 0) {
                break;
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
