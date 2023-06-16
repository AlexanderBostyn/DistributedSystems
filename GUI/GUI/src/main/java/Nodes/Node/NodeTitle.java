package Nodes.Node;

import javax.swing.*;
import java.awt.*;

public class NodeTitle extends JLabel {
    public NodeTitle(String name) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,screenSize.width,50);

        String title = "    " + name;
        setText(title);
        setHorizontalTextPosition(JLabel.LEFT);
        setVerticalTextPosition(JLabel.TOP);
        setFont(new Font("Comfort", Font.BOLD,20));
        setBackground(new Color(240, 240, 240, 255));
        setOpaque(true);
    }
}
