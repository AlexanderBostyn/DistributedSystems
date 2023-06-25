package Files;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FilesMain extends JPanel {
    public FilesMain(int i) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        setBackground(new Color(0, 20, 75));
        setOpaque(true);

        setLayout(null);
        add(new FilesTitle(i));
        add(new FilesCardPanel(i));
    }
}