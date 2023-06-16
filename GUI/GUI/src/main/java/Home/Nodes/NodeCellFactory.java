package Home.Nodes;

import javax.swing.*;
import java.awt.*;

public class NodeCellFactory {
    public NodeCellFactory() {}

    public JLabel createFirstCell() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("Comfort", Font.BOLD,15));
                g.setColor(new Color(255, 255, 255, 255));
                g.drawString("Name", 10, 50);
                g.drawString("ID", 160, 50);
                g.drawString("IP", 350, 50);
                g.drawString("Status", 525, 50);

                int lineY = 53;
                int lineX11 = 10;
                int lineX12 = 10 + g.getFontMetrics().stringWidth("Name");

                int lineX21 = 160;
                int lineX22 = 160 + g.getFontMetrics().stringWidth("ID");

                int lineX31 = 350;
                int lineX32 = 350 + g.getFontMetrics().stringWidth("IP");

                int lineX41 = 525;
                int lineX42 = 525 + g.getFontMetrics().stringWidth("Status");

                g.drawLine(lineX11, lineY, lineX12, lineY);
                g.drawLine(lineX21, lineY, lineX22, lineY);
                g.drawLine(lineX31, lineY, lineX32, lineY);
                g.drawLine(lineX41, lineY, lineX42, lineY);

            }
        };
        label.setBackground(new Color(5, 30, 120, 180));
        label.setOpaque(true);
        return label;
    }

    public JLabel createCell(String name, String id, String ip, String status) {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("Comfort", Font.BOLD,15));
                g.setColor(new Color(255, 255, 255, 255));
                g.drawString(name, 10, 50);
                g.drawString(id, 160, 50);
                g.drawString(ip, 350, 50);
                g.drawString(status, 525, 50);
            }
        };
        label.setBackground(new Color(5, 30, 120, 180));
        label.setOpaque(true);
        return label;
    }
}
