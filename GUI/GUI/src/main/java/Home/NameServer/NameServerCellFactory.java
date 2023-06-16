package Home.NameServer;

import javax.swing.*;
import java.awt.*;

public class NameServerCellFactory {
    public NameServerCellFactory() {}
    public JLabel createCell(String s1, String s2) {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("Comfort", Font.BOLD,15));
                g.setColor(new Color(255, 255, 255, 255));
                g.drawString(s1, 10, 20);
                g.drawString(s2, 600, 20);
            }
        };
        label.setBackground(new Color(5, 30, 120, 180));
        label.setOpaque(true);
        return label;
    }
}