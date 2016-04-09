package AUHack2016;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static KeyHandler handler;

    public static void main(String[] args) throws IOException {
        handler = new KeyHandler();

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
        GlobalScreen.addNativeKeyListener(new KeyLogger(handler));
    }
}
