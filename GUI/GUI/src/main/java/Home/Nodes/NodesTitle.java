package Home.Nodes;

import javax.swing.*;
import java.awt.*;

public class NodesTitle extends JLabel {
    public NodesTitle() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,screenSize.width,50);

        String title = "    Nodes";
        setText(title);
        setHorizontalTextPosition(JLabel.LEFT);
        setVerticalTextPosition(JLabel.TOP);
        setFont(new Font("Comfort", Font.BOLD,20));
        setBackground(new Color(240, 240, 240, 255));
        setOpaque(true);
    }
}
