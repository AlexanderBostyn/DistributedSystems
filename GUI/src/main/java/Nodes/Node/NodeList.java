package Nodes.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NodeList extends JPanel {
    public NodeList(String s) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,(screenSize.width - 450)/4, screenSize.height/3 - 50 - (screenSize.height/9 - 40) + 150);
        setOpaque(false);
        setLayout(new BorderLayout());

        add(addInfo(s));
    }

    public JLabel addInfo(String s) {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("Comfort", Font.BOLD,15));
                g.setColor(new Color(255, 255, 255, 255));
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                String[] strings = s.split(";");
                int x = 10;
                int x2 = (screenSize.width - 450)/7;
                g.drawString("Node name:", x, 20);
                g.drawString("Node id:", x, 86);
                g.drawString("Node ip:", x, 152);
                g.drawString("Prev node:", x, 218);
                g.drawString("Next node:", x, 284);
                g.drawString("Node status:", x, 350);
                g.drawString(strings[0], x2, 20);
                g.drawString(strings[1], x2, 86);
                g.drawString(strings[2], x2, 152);
                g.drawString(strings[3], x2, 218);
                g.drawString(strings[4], x2, 284);
                g.drawString(strings[5], x2, 350);
            }
        };
        label.setBackground(new Color(5, 30, 120, 180));
        label.setOpaque(true);
        return label;
    }
}
