package MainPanel;

import javax.swing.*;
import java.awt.*;

public class ImageLabel extends JLabel {
    public ImageLabel() {
        ImageIcon imageIcon = new ImageIcon("src/Images/UA.png");
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(75,75, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newImage);
        setIcon(imageIcon);

        setBounds(0,0,imageIcon.getIconWidth(),imageIcon.getIconHeight());
        setOpaque(true);
    }
}
