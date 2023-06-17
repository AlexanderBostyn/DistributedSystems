package Home.Files;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FilesList extends JPanel {
    public FilesCardPanel filesCardPanel;
    public int numRows = 1;
    public FilesList(FilesCardPanel fcp, int startPositie, ArrayList<String> fileList, int filesLeft) {
        filesCardPanel = fcp;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height/2 - 50);
        setOpaque(false);

        setLayout(new GridLayout(10,1, 0,5));

        FilesCellFactory fcf = new FilesCellFactory();
        add(fcf.createFirstCell());

        while(numRows < 10) {
            if (numRows == 9) {
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
            }
            else if(filesLeft == 0) {
                break;
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
