package Nodes.Node;

import MainPanel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NodeButtons extends JPanel {
    public MainPanel mainPanel;
    public ArrayList<Boolean> status;
    public NodeButtons(MainPanel mp, int i, ArrayList<Boolean> status) {
        this.status = status;
        mainPanel = mp;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(10, 2*screenSize.height/9 + 50, (screenSize.width - 450)/4 - 20, screenSize.height/9 - 60);
        setBackground(new Color(0, 15, 30));
        setLayout(new GridLayout(1,2));

        JButton files = new JButton("Files");
        files.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.showPanel(1+i);
            }
        });
        JButton switchbtn = new JButton("Turn on/off");
        switchbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(status.get(i-1)) {
                    JOptionPane.showMessageDialog(null,"Node shutdown","Node Shutdown",JOptionPane.INFORMATION_MESSAGE);
                    status.set(i-1,false);
                } else {
                    JOptionPane.showMessageDialog(null,"Node Startup","Node Startup",JOptionPane.INFORMATION_MESSAGE);
                    status.set(i-1,true);
                }
                mainPanel.refresh(status);
                mainPanel.showPanel(1);
            }
        });

        add(files);
        add(switchbtn);
    }
}
