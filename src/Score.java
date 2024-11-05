/*
 * Score.java
 * Ario Barin Ostovary
 * This class contains the score of the player
 */

class Score implements Comparable<Score> {
    private String name;
    private int score;

    public Score(String n, int s) {
        name = n;
        score = s;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Score s2) {
        return s2.score - this.score;
    }

    public String twoLine() {
        return String.format("%s\n%d", name, score);
    }

    @Override
    public String toString() {
        return String.format("%-20s%5d", name, score);
    }
}