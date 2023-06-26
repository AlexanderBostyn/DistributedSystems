package Files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilesCellFactory {
    int x1 = 10;
    int x2 = 75+65;
    int x3 = 275+65+200;
    int x4 = 425+65+200+150;
    int x5 = 555+65+200+150+130;
    public FilesCellFactory() {}

    public JLabel createFirstCell() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int y = 25;
                g.setFont(new Font("Comfort", Font.BOLD,15));
                g.setColor(new Color(255, 255, 255, 255));
                g.drawString("Hash", x1, y);
                g.drawString("Name", x2, y);
                g.drawString("Origin", x3, y);
                g.drawString("Owner", x4, y);
                g.drawString("Locked?", x5, y);

                int lineY = y+3;
                int lineX11 = x1;
                int lineX12 = x1 + g.getFontMetrics().stringWidth("Hash");

                int lineX21 = x2;
                int lineX22 = x2 + g.getFontMetrics().stringWidth("Name");

                int lineX31 = x3;
                int lineX32 = x3 + g.getFontMetrics().stringWidth("Origin");

                int lineX41 = x4;
                int lineX42 = x4 + g.getFontMetrics().stringWidth("Owner");

                int lineX51 = x5;
                int lineX52 = x5 + g.getFontMetrics().stringWidth("Locked?");

                g.drawLine(lineX11, lineY, lineX12, lineY);
                g.drawLine(lineX21, lineY, lineX22, lineY);
                g.drawLine(lineX31, lineY, lineX32, lineY);
                g.drawLine(lineX41, lineY, lineX42, lineY);
                g.drawLine(lineX51, lineY, lineX52, lineY);

            }
        };
        label.setBackground(new Color(5, 30, 120, 180));
        label.setOpaque(true);
        return label;
    }

    public JLabel createCell(String hash, String name, String origin, String owner, String status) {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                int y = 30;
                super.paintComponent(g);
                g.setFont(new Font("Comfort", Font.BOLD,15));
                g.setColor(new Color(255, 255, 255, 255));
                g.drawString(hash, x1, y);
                g.drawString(name, x2, y);
                g.drawString(origin, x3, y);
                g.drawString(owner, x4, y);
                g.drawString(status, x5, y);
            }
        };
        label.setBackground(new Color(5, 30, 120, 180));
        label.setOpaque(true);
        return label;
    }

    public JLabel createBtnCell(FilesCardPanel fcp) {
        JLabel label = new JLabel();
        label.setBackground(new Color(5, 30, 120, 180));
        label.setOpaque(true);
        label.setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JButton next = new JButton("Next");
        next.setBounds((screenSize.width - 300)/2,0,(screenSize.width - 300)/2,50);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fcp.showNextPanel();
            }
        });
        JButton previous = new JButton("Previous");
        previous.setBounds(0,0,(screenSize.width - 300)/2,50);
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fcp.showPreviousPanel();
            }
        });

        label.add(next);
        label.add(previous);

        return label;
    }

    public JLabel createEmptyCell() {
        JLabel label = new JLabel();
        label.setBackground(new Color(5, 30, 120, 180));
        label.setOpaque(true);
        label.setLayout(null);
        return label;
    }
}
