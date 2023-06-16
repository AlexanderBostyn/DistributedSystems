package Nodes.Node;

import MainPanel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NodeButtons extends JPanel {
    public MainPanel mainPanel;
    public NodeButtons(MainPanel mp) {
        mainPanel = mp;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(10, 2*screenSize.height/9 + 50, (screenSize.width - 450)/4 - 20, screenSize.height/9 - 60);
        setBackground(new Color(0, 15, 30));
        setLayout(new GridLayout(1,2));

        JButton files = new JButton("Files");
        files.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.showPanel(2);
            }
        });
        JButton switchbtn = new JButton("Turn on/off");
        switchbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Node shutdown","Node Shutdown",JOptionPane.WARNING_MESSAGE);
                //mainPanel.showPanel(2);
            }
        });

        add(files);
        add(switchbtn);
    }
}
