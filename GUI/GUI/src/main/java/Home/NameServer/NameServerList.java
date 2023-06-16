package Home.NameServer;

import javax.swing.*;
import java.awt.*;

public class NameServerList extends JPanel {
    public NameServerList() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height/4 - 50);
        setOpaque(false);

        setLayout(new GridLayout(6,1, 0,5));

        NameServerCellFactory nscf = new NameServerCellFactory();

        add(nscf.createCell("Status", "Online"));
        add(nscf.createCell("Nodes", "5"));
        add(nscf.createCell("Discovery", "Online"));
        add(nscf.createCell("Replication", "Online"));
        add(nscf.createCell("Sync Agent", "Offline"));
        add(nscf.createCell("Failure Agent", "Offline"));
    }
}
