package AUHack2016;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * Created by Thomas on 09-04-2016.
 */
public class KeyLogger implements NativeKeyListener {
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));

        /* Terminate program when one press ESCAPE */
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }
}
