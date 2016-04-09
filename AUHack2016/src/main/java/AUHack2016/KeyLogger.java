package AUHack2016;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * Created by Thomas on 09-04-2016.
 */
public class KeyLogger implements NativeKeyListener {
    interface IKeyloggerCallback {
        void onWordTyped(String word);

        void onSentenceTyped(String sentence);
    }

    private IKeyloggerCallback callback;
    private String word, sentence;

    KeyLogger(IKeyloggerCallback callback) {
        this.callback = callback;
        word = sentence = "";
    }

    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int keyCode = nativeKeyEvent.getKeyCode();

        boolean isSpecial = false;
        if (isWordSeparator(keyCode) || isNewline(keyCode)) {
            String tmpWord = word.trim().toLowerCase();
            if (!word.isEmpty()) {
                callback.onWordTyped(tmpWord);
                sentence += tmpWord + " ";
            }
            word = "";
            isSpecial = true;
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
            }
        }

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
}
