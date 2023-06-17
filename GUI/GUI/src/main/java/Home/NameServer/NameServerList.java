package Home.NameServer;

import Data.DataContainer;

import javax.swing.*;
import java.awt.*;

public class NameServerList extends JPanel {
    public NameServerList() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height/4 - 50);
        setOpaque(false);

        setLayout(new GridLayout(6,1, 0,5));

        NameServerCellFactory nscf = new NameServerCellFactory();

        DataContainer dataContainer = new DataContainer();
        String[] strings = dataContainer.getNameServer().split(";");

        add(nscf.createCell("Status", strings[0]));
        add(nscf.createCell("Nodes", strings[1]));
        add(nscf.createCell("Discovery", strings[2]));
        add(nscf.createCell("Replication", strings[3]));
        add(nscf.createCell("Sync Agent", strings[4]));
        add(nscf.createCell("Failure Agent", strings[5]));
    }
}
