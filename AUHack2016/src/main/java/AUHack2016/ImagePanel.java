package AUHack2016;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Thomas on 09-04-2016.
 */
public class ImagePanel extends JComponent {
    private Image image;
    public int IMG_WIDTH = 500;
    public int IMG_HEIGHT = 400;

    public ImagePanel() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;

        this.IMG_WIDTH = (int) (image.getWidth() * 0.80);
        this.IMG_HEIGHT = (int) (image.getHeight() * 0.80);
        this.image = this.image.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_DEFAULT);

        this.revalidate();
        this.repaint();
    }
}

