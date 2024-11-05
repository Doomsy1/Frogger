/*
 * ScoreManager.java
 * Ario Barin Ostovary
 * Manages loading, saving, and updating high scores
 */

import java.io.*;
import java.util.*;

public class ScoreManager {
    private ArrayList<Score> highScores;
    private final String scoreFile = "scores.txt";

    public ScoreManager() {
        highScores = loadScores();
    }

    public ArrayList<Score> getHighScores() {
        return highScores;
    }

    public int getHighestScore() {
        if (highScores.isEmpty()) {
            return 0;
        }
        return highScores.get(0).getScore();
    }

    public void addScore(Score score) {
        // if the player's score is already in the high scores, keep the highest score
        if (highScores.isEmpty()) {
            highScores.add(score);
            return;
        }
        
        Score existingScore = null;
        for (Score sc : highScores) {
            if (sc.getName().equals(score.getName())) {
                existingScore = sc;
                break;
            }
        }
        
        if (existingScore != null) {
            if (existingScore.getScore() <= score.getScore()) {
                highScores.remove(existingScore);
                highScores.add(score);
            }
        } else {
            highScores.add(score);
        }
        
        Collections.sort(highScores);
        if (highScores.size() > 10) {
            highScores = new ArrayList<>(highScores.subList(0, 10));
        }
    }

    public void saveScores() {
        try {
            PrintWriter out = new PrintWriter(new File(scoreFile));
            for (Score sc : highScores) {
                out.println(sc.twoLine());
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error saving scores: " + e);
        }
    }

    private ArrayList<Score> loadScores() {
        ArrayList<Score> scores = new ArrayList<>();
        try {
            File file = new File(scoreFile);
            if (file.exists()) {
                Scanner input = new Scanner(file);
                while (input.hasNextLine()) {
                    String name = input.nextLine();
                    if (input.hasNextLine()) {
                        int points = Integer.parseInt(input.nextLine());
                        scores.add(new Score(name, points));
                    }
                }
                input.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading scores: " + e);
        }
        return scores;
    }
}