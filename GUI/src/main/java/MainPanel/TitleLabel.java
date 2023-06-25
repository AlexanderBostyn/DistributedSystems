package MainPanel;

import javax.swing.*;
import java.awt.*;

public class TitleLabel extends JLabel {
    public TitleLabel()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(75,0,screenSize.width,75);

        String title = "NODE NETWORK";
        setText(title);
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.TOP);
        setForeground(new Color(250, 2, 2, 180));
        setFont(new Font("Comfort", Font.BOLD,50));

        setIconTextGap(0);
        setBackground(new Color(255, 255, 255, 255));
        setOpaque(true);
        setVerticalAlignment(JLabel.TOP);
        setHorizontalAlignment(JLabel.CENTER);
    }

}
