package AUHack2016;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by Thomas on 09-04-2016.
 */
public class KeyLogger implements NativeKeyListener {
    interface IKeyloggerCallback {
        void onWordTyped(String word);

        void onSentenceTyped(String sentence);

        void onWordRemoved(String word);
    }

    private IKeyloggerCallback callback;
    private String word, sentence, removedCharacters;
    private int lastKeyPress;

    KeyLogger(IKeyloggerCallback callback) {
        this.callback = callback;
        removedCharacters = word = sentence = "";
    }

    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int keyCode = nativeKeyEvent.getKeyCode();
        //System.out.println(NativeKeyEvent.getKeyText(keyCode));

        if (keyCode == NativeKeyEvent.VC_BACKSPACE || keyCode == NativeKeyEvent.VC_DELETE) {
            handleDeleteCharacter(keyCode);
            return;
        }

        boolean isSpecial = false;
        if (isWordSeparator(keyCode) || isNewline(keyCode)) {
            String tmpWord = word.trim().toLowerCase();
            if (!word.isEmpty()) {
                callback.onWordTyped(tmpWord);
                sentence += tmpWord + " ";
            }
            word = "";
            isSpecial = true;
            handleDeleteComplete();
        }
        if (isNewline(keyCode)) {
            String trimSentence = sentence.trim();
            if (!trimSentence.isEmpty()) {
                callback.onSentenceTyped(trimSentence);
            }
            sentence = "";
            isSpecial = true;
        }

        // TODO dont do like this, start thinking and do something better!
        if (!isSpecial) {
            String keyStroke = NativeKeyEvent.getKeyText(keyCode);
            if (isSingleCharacter(keyStroke)) {
                word += keyStroke;
                handleDeleteComplete();
            }
        }

        lastKeyPress = keyCode;

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

    private boolean isSingleCharacter(String keyText) {
        // TODO look at keycode instead (Between 18 and 50?)
        return keyText.length() == 1;

    }

    private boolean isWordSeparator(int keyCode) {
        return keyCode == NativeKeyEvent.VC_SPACE;
    }

    private boolean isNewline(int keyCode) {
        return keyCode == NativeKeyEvent.VC_ENTER || keyCode == NativeKeyEvent.VC_PERIOD;
    }

    private void handleDeleteComplete() {
        if (removedCharacters.trim().isEmpty()) {
            removedCharacters = "";
            return;
        }

        callback.onWordRemoved(new StringBuilder(removedCharacters.trim().toLowerCase()).reverse().toString());
        removedCharacters = "";
    }


    private void handleDeleteCharacter(int keyCode) {
        NativeKeyEvent ctrlZ = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                System.currentTimeMillis(),
                NativeKeyEvent.CTRL_L_MASK,        // Modifiers
                0x00,        // Raw Code
                NativeKeyEvent.VC_Z,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_STANDARD);

        NativeKeyEvent shiftLeft = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                System.currentTimeMillis(),
                NativeKeyEvent.SHIFT_L_MASK,        // Modifiers
                0x00,        // Raw Code
                NativeKeyEvent.VC_LEFT,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_STANDARD);

        NativeKeyEvent left = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                System.currentTimeMillis(),
                0x00,        // Modifiers
                0x00,        // Raw Code
                NativeKeyEvent.VC_LEFT,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_STANDARD);

        NativeKeyEvent shiftRight = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                System.currentTimeMillis(),
                NativeKeyEvent.SHIFT_L_MASK,        // Modifiers
                0x00,        // Raw Code
                NativeKeyEvent.VC_RIGHT,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_STANDARD);

        NativeKeyEvent copy = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                System.currentTimeMillis(),
                NativeKeyEvent.CTRL_L_MASK,        // Modifiers
                0x00,        // Raw Code
                NativeKeyEvent.VC_C,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_STANDARD);

        NativeKeyEvent delete = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                System.currentTimeMillis(),
                0x00,        // Modifiers
                0x00,        // Raw Code
                NativeKeyEvent.VC_DELETE,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_STANDARD);

        NativeKeyEvent backspace = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                System.currentTimeMillis(),
                0x00,        // Modifiers
                0x00,        // Raw Code
                NativeKeyEvent.VC_BACKSPACE,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_STANDARD);


        synchronized (this) {
            try {
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException e) {
                    e.printStackTrace();
                }
                GlobalScreen.postNativeEvent(ctrlZ);
                Thread.sleep(50);
                if (keyCode == NativeKeyEvent.VC_BACKSPACE) {
                    GlobalScreen.postNativeEvent(shiftLeft);
                    Thread.sleep(50);
                    GlobalScreen.postNativeEvent(copy);
                    Thread.sleep(50);
                    GlobalScreen.postNativeEvent(delete);
                } else {
                    GlobalScreen.postNativeEvent(shiftRight);
                    Thread.sleep(50);
                    GlobalScreen.postNativeEvent(copy);
                    Thread.sleep(50);
                    GlobalScreen.postNativeEvent(backspace);
                }

                try {
                    GlobalScreen.registerNativeHook();
                } catch (NativeHookException e) {
                    e.printStackTrace();
                }

                String deletedKey = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                //System.out.println(deletedKey);
                removedCharacters += deletedKey;
                if (deletedKey.trim().isEmpty()) {
                    handleDeleteComplete();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            }
        }
    }
}
