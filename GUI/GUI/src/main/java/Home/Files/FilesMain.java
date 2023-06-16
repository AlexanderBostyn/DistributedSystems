package Home.Files;

import javax.swing.*;
import java.awt.*;

public class FilesMain extends JPanel {
    public FilesMain() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(150 + (screenSize.width-450)/2, 75 + screenSize.height/4, (screenSize.width - 450)/2, screenSize.height/2);
        setBackground(new Color(0, 12, 28));
        setOpaque(false);

        setLayout(null);
        add(new FilesTitle());
        add(new FilesCardPanel());
    }
}
