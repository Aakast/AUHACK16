package AUHack2016;

import javazoom.jl.player.Player;

import java.net.URL;

/**
 * Created by Thomas on 09-04-2016.
 */
public class SoundHandler {
    public static void playSound(KeyHandler.Severity severity) {
        Thread t = new Thread(() -> {
            try {
                URL url = new URL(String.format("http://www.thomasheine.dk/words/%s.mp3", severity.name()));
                Player playMP3 = new Player(url.openStream());
                playMP3.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    }
}
