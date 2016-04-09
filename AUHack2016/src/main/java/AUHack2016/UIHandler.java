package AUHack2016;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Thomas on 09-04-2016.
 */
public class UIHandler {
    private JDialog popup;
    private JFrame staticFrame;
    private HashMap<KeyHandler.Severity, JLabel> severityList;
    private HashMap<KeyHandler.Severity, KeyHandler> handlerList;
    private ImagePanel imagePanel;
    private JLabel labelTotal;
    private int minutes = 1;
    public JLabel timer;

    private static UIHandler ourInstance = new UIHandler();

    public static UIHandler getInstance() {
        return ourInstance;
    }

    private UIHandler() {

    }

    public void configureImageFrame() {
        popup = new JDialog(staticFrame);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        this.imagePanel = new ImagePanel();
        this.imagePanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popup.setVisible(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        mainPanel.add(this.imagePanel);

        popup.setContentPane(mainPanel);

        popup.setSize(600, 600);
        popup.setAlwaysOnTop(true);
        popup.setUndecorated(true);
        popup.setLocationRelativeTo(null);
        popup.setBackground(new Color(1, 1, 1, 0.0f));
        popup.setVisible(true);
    }

    public void configureStaticFrame(ArrayList<KeyHandler> handlers) {
        staticFrame = new JFrame("Statistics");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        timer = new JLabel("Time: 0:0");
        mainPanel.add(timer);

        handlerList = new HashMap<>();
        for (KeyHandler handler : handlers) {
            handlerList.put(handler.getSeverity(), handler);
        }

        severityList = new HashMap<>();
        for (KeyHandler.Severity severity : KeyHandler.Severity.values()) {
            JLabel label = new JLabel(getSeverityText(severity, 0));
            severityList.put(severity, label);
            mainPanel.add(label);
        }

        labelTotal = new JLabel(getTotalText());
        mainPanel.add(labelTotal);

        staticFrame.setContentPane(mainPanel);
        staticFrame.setAlwaysOnTop(true);
        staticFrame.setUndecorated(true);
        staticFrame.setLocation(0, 0);
        staticFrame.setSize(325, 90);
        staticFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        staticFrame.setBackground(new Color(1, 1, 1, 0.8f));
        staticFrame.setVisible(true);
    }

    public void showUIMessage(KeyHandler.Severity severity, int hitCount) {
        // Clear existing content
        if (this.popup == null) {
            this.configureImageFrame();
        }

        SoundHandler.playSound(severity);

        try {
            int index = new Random().nextInt(7);
            InputStream input = new URL(String.format("http://www.thomasheine.dk/words/pop-up_%s_%s.png", severity.name(), index)).openStream();
            BufferedImage image = ImageIO.read(input);
            this.imagePanel.setImage(image);

            this.popup.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateWordCount(KeyHandler.Severity severity, int totalWordCount) {
        severityList.get(severity).setText(getSeverityText(severity, totalWordCount));
        updateTotal();
    }

    private String getSeverityText(KeyHandler.Severity severity, int wordCount) {
        return "Severity: " + severity + ". Word count: " + wordCount + ". Word/min: " + wordCount / minutes;
    }

    public void addMinute() {
        minutes++;
        for (KeyHandler.Severity severity : KeyHandler.Severity.values()) {
            KeyHandler handler = handlerList.get(severity);
            severityList.get(severity).setText(getSeverityText(severity, handler.getTotalWordCount()));
        }
        updateTotal();
    }

    public String getTotalText() {
        int words = 0;
        for (KeyHandler handler : handlerList.values()) {
            words += handler.getTotalWordCount();
        }

        return "Total words: " + words + ". Word/min: " + (words / minutes);
    }

    private void updateTotal() {
        labelTotal.setText(getTotalText());
    }

    public void showUIWarning(KeyHandler.Severity severity) {
        try {
            System.out.println("Warning: " + severity.name());
            InputStream input = new URL(String.format("http://www.thomasheine.dk/words/warning_%s.png", severity.name())).openStream();
            BufferedImage image = ImageIO.read(input);
            this.imagePanel.setImage(image);

            this.popup.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUIShutdown(KeyHandler.Severity severity) {
        try {
            System.out.println("Shutdown: " + severity.name());
            Runtime runtime = Runtime.getRuntime();
            OSValidator validator = new OSValidator();
            if (validator.isWindows()) {
                Process proc = runtime.exec("shutdown -s -t 5 -c \"You have been swearing too much, the computer will shutdown now!\"");
            } else if (validator.isMac()) {
                Process proc = runtime.exec("shutdown now");
            } else if (validator.isUnix()) {
                Process proc = runtime.exec("sleep 10;shutdown now");
            } else {
                //Process proc = runtime.exec("shutdown -s -t 5 -c \"You have been swearing too much, the computer will shutdown now!\"");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showKarma(KeyHandler.Severity severity) {
        System.out.println("Show karma: " + severity);

        try {
            String urlStr = String.format("http://www.thomasheine.dk/words/karma_%s.gif", severity.name());

            URL url = new URL(urlStr);
            Icon icon = new ImageIcon(url);
            JLabel label = new JLabel(icon);

            final JFrame f = new JFrame("Animation");
            f.getContentPane().add(label);
            f.setUndecorated(true);
            f.setBackground(new Color(0, 0, 0, 0.0f));
            f.setLocation(0, 0);
            f.setAlwaysOnTop(true);

            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            f.pack();
            int x = (int) ((dimension.getWidth() - f.getWidth()) / 2);
            f.setLocation(x, 0);

            /*ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
            s.schedule(() -> {
                f.setVisible(false);
                f.dispose();
            }, 4000, TimeUnit.MILLISECONDS);*/

            f.setVisible(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
