package MainPanel;

import javax.swing.*;
import java.awt.*;

public class TitleLabel extends JLabel {
    public TitleLabel()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0,0,screenSize.width,75);
        //this.setLayout(new BorderLayout(10,0));
        this.setForeground(new Color(0, 20, 75, 175));

        String title = "NODE NETWORK";
        this.setText(title);
        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setVerticalTextPosition(JLabel.TOP);
        this.setForeground(new Color(250, 2, 2, 180));
        this.setFont(new Font("Comfort", Font.BOLD,50));

        ImageIcon imageIcon = new ImageIcon("src/Images/UA.png");
        this.setIcon(imageIcon);

        //Change the position of the icon and title and set the background
        this.setIconTextGap(0);
        this.setBackground(new Color(255, 255, 255, 255));
        this.setOpaque(true);
        this.setVerticalAlignment(JLabel.TOP);
        this.setHorizontalAlignment(JLabel.CENTER);
    }

}
