package Files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilesCellFactory {
    public FilesCellFactory() {}

    public JLabel createFirstCell() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int y = 35;
                g.setFont(new Font("Comfort", Font.BOLD,15));
                g.setColor(new Color(255, 255, 255, 255));
                g.drawString("Hash", 10, y);
                g.drawString("Name", 75+65, y);
                g.drawString("Origin", 275+65+200, y);
                g.drawString("Owner", 425+65+200+150, y);
                g.drawString("Status", 555+65+200+150+130, y);

                int lineY = y+3;
                int lineX11 = 10;
                int lineX12 = 10 + g.getFontMetrics().stringWidth("Hash");

                int lineX21 = 75+65;
                int lineX22 = 75+65 + g.getFontMetrics().stringWidth("Name");

                int lineX31 = 275+65+200;
                int lineX32 = 275+65+200 + g.getFontMetrics().stringWidth("Origin");

                int lineX41 = 425+65+200+150;
                int lineX42 = 425+65+200+150 + g.getFontMetrics().stringWidth("Owner");

                int lineX51 = 555+65+200+150+130;
                int lineX52 = 555+65+200+150+130 + g.getFontMetrics().stringWidth("Status");

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
                int y = 35;
                super.paintComponent(g);
                g.setFont(new Font("Comfort", Font.BOLD,15));
                g.setColor(new Color(255, 255, 255, 255));
                g.drawString(hash, 10, y);
                g.drawString(name, 75+65, y);
                g.drawString(origin, 275+65+200, y);
                g.drawString(owner, 425+65+200+150, y);
                g.drawString(status, 555+65+200+150+130, y);
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
        next.setBounds((screenSize.width - 450)/2 + 25,0,(screenSize.width - 450)/2 + 25,40);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fcp.showNextPanel();
            }
        });
        JButton previous = new JButton("Previous");
        previous.setBounds(0,0,(screenSize.width - 450)/2 + 25,40);
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
