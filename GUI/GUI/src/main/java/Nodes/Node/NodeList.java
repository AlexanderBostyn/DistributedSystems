package Nodes.Node;

import Home.Nodes.NodeCellFactory;

import javax.swing.*;
import java.awt.*;

public class NodeList extends JPanel {
    public NodeList() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,(screenSize.width - 450)/4, screenSize.height/3 - 50 - (screenSize.height/9 - 40));
        setOpaque(false);
        setLayout(new BorderLayout());

        add(addInfo());
    }

    public JLabel addInfo() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("Comfort", Font.BOLD,15));
                g.setColor(new Color(255, 255, 255, 255));
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int x = 10;
                int x2 = (screenSize.width - 450)/6;
                g.drawString("Node name:", x, 20);
                g.drawString("Node id:", x, 86);
                g.drawString("Node ip:", x, 152);
                g.drawString("Node status:", x, 218);
                g.drawString("Node name:", x2, 20);
                g.drawString("Node id:", x2, 86);
                g.drawString("Node ip:", x2, 152);
                g.drawString("Node status:", x2, 218);
            }
        };
        label.setBackground(new Color(5, 30, 120, 180));
        label.setOpaque(true);
        return label;
    }
}
