package AUHack2016;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 09-04-2016.
 */
public class AchievementHandler {
    private List<Achievement> deletedWords;
    private int deletedWordsCount;
    private List<Achievement> completedSentences;
    private int completedSentencesCount;

    private static AchievementHandler ourInstance = new AchievementHandler();

    public static AchievementHandler getInstance() {
        return ourInstance;
    }


    private AchievementHandler() {
        this.deletedWords = new ArrayList<>();
        this.deletedWords.add(new Achievement("ach-delete-2", 2));
        this.deletedWords.add(new Achievement("ach-delete-5", 5));
        this.deletedWords.add(new Achievement("ach-delete-10", 10));

        this.completedSentences = new ArrayList<>();
        this.completedSentences.add(new Achievement("ach-sent-2", 2));
        this.completedSentences.add(new Achievement("ach-sent-5", 5));
        this.completedSentences.add(new Achievement("ach-sent-10", 10));
    }

    public void removedSwearWord() {
        this.deletedWordsCount++;
        for (Achievement ach : deletedWords) {
            ach.tryAchieve(this.deletedWordsCount);
        }
    }

    public void completedSentence() {
        this.completedSentencesCount++;
        for (Achievement ach : completedSentences) {
            ach.tryAchieve(this.completedSentencesCount);
        }
    }

    private class Achievement {
        private int value;
        private String imageName;
        private boolean hasAchieved;

        public Achievement(String imageName, int value) {
            this.imageName = imageName;
            this.value = value;
        }

        public void tryAchieve(int input) {
            boolean upNext = !hasAchieved && input >= value;
            if (upNext) {
                this.hasAchieved = true;
                UIHandler.getInstance().showAchievement(imageName);
            }
        }
    }
}
