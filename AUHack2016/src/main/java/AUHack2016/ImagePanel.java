package AUHack2016;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Thomas on 09-04-2016.
 */
public class ImagePanel extends JComponent {
    private Image image;

    public ImagePanel() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    public void setImage(Image image) {
        this.image = image;
        this.revalidate();
        this.repaint();
    }
}

