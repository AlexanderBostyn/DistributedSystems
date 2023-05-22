package Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel implements ActionListener {
    //TODO: adds a panel with all the options

    Button Home;
    Button Nodes;
    Button Files;

    public MenuPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, 200, screenSize.height);
        this.setBackground(new Color(0, 20, 75, 175));

        this.Home = new Button("Home");
        this.Home.setPreferredSize(new Dimension(100, 50));
        this.Home.addActionListener(this);
        this.Nodes = new Button("Nodes");
        this.Nodes.setPreferredSize(new Dimension(100, 50));
        this.Nodes.addActionListener(this);
        this.Files = new Button("Files");
        this.Files.setPreferredSize(new Dimension(100, 50));
        this.Files.addActionListener(this);

        this.add(Home);
        this.add(Nodes);
        this.add(Files);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
