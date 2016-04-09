package AUHack2016;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static KeyHandlerManager manager;
    private static ArrayList<KeyHandler> handlers;

    public static void main(String[] args) throws IOException {
        manager = new KeyHandlerManager();
        handlers = new ArrayList<>();
        handlers.add(new KeyHandler("soft.txt", KeyHandler.Severity.Soft));
        handlers.add(new KeyHandler("medium.txt", KeyHandler.Severity.Medium));
        handlers.add(new KeyHandler("strong.txt", KeyHandler.Severity.Strong));

        UIHandler.getInstance().configureStaticFrame(handlers);

        try {
            /* Register jNativeHook */
            GlobalScreen.registerNativeHook();

            // Get the logger for "org.jnativehook" and set the level to off.
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
        } catch (NativeHookException ex) {
            /* Its error */
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        /* Construct the example object and initialize native hook. */
        GlobalScreen.addNativeKeyListener(new KeyLogger(manager));
    }

    private static class KeyHandlerManager implements KeyLogger.IKeyloggerCallback {
        private Timer timer;
        private Instant startTime;
        private int time = 0;
        private int warningTimer = 0;

        public KeyHandlerManager() {
            startTime = Instant.now();
            this.timer = new Timer();
            this.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    time++;
                    if (time >= 60) {
                        UIHandler.getInstance().addMinute();
                        time = 0;
                        warningTimer++;
                    }

                    // Clear after 5 minutes
                    if (warningTimer >= (60 * 5)) {
                        handlers.forEach(KeyHandler::clearCounters);
                    }
                    Duration dur = Duration.between(startTime, Instant.now());
                    DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_TIME;
                    LocalTime fTime = LocalTime.ofNanoOfDay(dur.toNanos());
                    UIHandler.getInstance().timer.setText("Time: " + fTime.format(df));
                }
            }, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));
        }

        @Override
        public void onWordTyped(String word) {
            for (KeyHandler kh : handlers) {
                kh.onWordTyped(word);
            }
        }

        @Override
        public void onSentenceTyped(String sentence) {

            boolean containsSwearWords = false;
            sentence = sentence.trim().toLowerCase();
            if (sentence.isEmpty()) {
                return;
            }
            for (KeyHandler kh : handlers) {
                for (String word : kh.wordList) {
                    if (sentence.contains(word)) {
                        containsSwearWords = true;
                        break;
                    }
                }
                if (containsSwearWords) {
                    break;
                }
            }

            if (!containsSwearWords) {
                AchievementHandler.getInstance().completedSentence();
            }
        }

        @Override
        public void onWordRemoved(String word) {
            for (KeyHandler kh : handlers) {
                kh.onWordRemoved(word);
            }
        }
    }
}
