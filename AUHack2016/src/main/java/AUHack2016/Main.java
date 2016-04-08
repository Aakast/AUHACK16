package AUHack2016;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class Main {
    public static void main(String[] args) {
        try {
            /* Register jNativeHook */
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            /* Its error */
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        /* Construct the example object and initialize native hook. */
        GlobalScreen.addNativeKeyListener(new KeyLogger());
    }
}
