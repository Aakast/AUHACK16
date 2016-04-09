package AUHack2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 09-04-2016.
 */
public class KeyHandler implements KeyLogger.IKeyloggerCallback {
    private List<String> words;

    public KeyHandler() throws IOException {
        this.words = readWords();
    }

    private List<String> readWords() throws IOException {
        List<String> words = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("D:\\temp\\words.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            words.add(line.trim());
        }

        return words;
    }

    @Override
    public void onWordTyped(String word) {
        System.out.println("Word pressed: " + word);

        if (words.contains(word)) {
            System.out.println("IT IS A SWEAR WORD!");
        }
    }

    @Override
    public void onSentenceTyped(String sentence) {
        System.out.println("Sentence: " + sentence);
    }
}
