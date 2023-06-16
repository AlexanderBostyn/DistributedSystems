package Home.NameServer;

import javax.swing.*;
import java.awt.*;

public class NameServerTitle extends JLabel {
    public NameServerTitle() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,screenSize.width,50);

        String title = "    Nameserver";
        setText(title);
        setHorizontalTextPosition(JLabel.LEFT);
        setVerticalTextPosition(JLabel.TOP);
        setForeground(new Color(20, 20, 20));
        setFont(new Font("Comfort", Font.BOLD,20));
        setBackground(new Color(240, 240, 240, 255));
        setOpaque(true);
    }
}
