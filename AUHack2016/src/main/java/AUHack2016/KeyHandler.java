package AUHack2016;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class KeyHandler implements KeyLogger.IKeyloggerCallback {
    public enum Severity {
        Soft(3), Medium(2), Strong(1);

        int value;

        Severity(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private int warningAlert;
    private int warningCount;
    private List<String> wordList;
    public int totalWordCount;
    private int numberOfSwearWords;
    private Severity severity;

    public KeyHandler(String wordList, Severity severity) throws IOException {
        this.wordList = readWords(wordList);
        this.severity = severity;
        numberOfSwearWords = 0;
        warningAlert = 3 * severity.getValue();
        if (severity == Severity.Soft) {
            warningAlert = 2 * severity.getValue();
        }
    }

    private List<String> readWords(String wordList) throws IOException {
        List<String> words = new ArrayList<>();
        InputStream input = new URL("http://www.thomasheine.dk/words/" + wordList).openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = br.readLine()) != null) {
            words.add(line.trim());
        }

        return words;
    }

    @Override
    public void onWordTyped(String word) {
        if (wordList.contains(word)) {
            numberOfSwearWords++;
            totalWordCount++;
            System.out.println("Swear word: " + word + ". Severity: " + this.severity.name());
            UIHandler.getInstance().updateWordCount(this.severity, totalWordCount);
        }

        if (numberOfSwearWords >= severity.getValue()) {
            UIHandler.getInstance().showUIMessage(severity, totalWordCount);
            numberOfSwearWords = 0;
            warningCount++;

            if (warningCount == warningAlert) {
                UIHandler.getInstance().showUIWarning(severity);
            } else if (warningCount > warningAlert) {
                UIHandler.getInstance().showUIShutdown(severity);
                clearCounters();
            }
        }
    }

    @Override
    public void onSentenceTyped(String sentence) {
        //System.out.println("Sentence: " + sentence);
    }

    @Override
    public void onWordRemoved(String word) {
        System.out.println("Word removed: " + word);
        if (wordList.contains(word)) {
            UIHandler.getInstance().showKarma(severity);
        }
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public int getTotalWordCount() {
        return this.totalWordCount;
    }

    public void clearCounters() {
        this.warningCount = 0;
    }
}
