package Files;

import javax.swing.*;
import java.awt.*;

public class FilesTitle extends JLabel {
    public FilesTitle() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,screenSize.width,50);

        String title = "    Files of ...";
        setText(title);
        setHorizontalTextPosition(JLabel.LEFT);
        setVerticalTextPosition(JLabel.TOP);
        setForeground(new Color(20, 20, 20));
        setFont(new Font("Comfort", Font.BOLD,20));
        setBackground(new Color(240, 240, 240, 255));
        setOpaque(true);
    }
}
