/*
 * Score.java
 * Ario Barin Ostovary
 * This class contains the scores of the players.
 */

class Score implements Comparable<Score> {
    private String name;
    private int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Score other) {
        // Compare the scores
        return other.score - this.score;
    }

    public String twoLine() {
        return name + "\n" + score;
    }

    @Override
    public String toString() {
        return name + " " + score;
    }
}