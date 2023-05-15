package sk.tuke.gamestudio.tentrix.core;

public class ScoreCounter {
    private int score = 0;
    private int lastScoreIncrease = 0;
    private boolean scoreUpdated = false;

    public ScoreCounter() {

    }

    public int getScore() {
        scoreUpdated = false;
        return score;
    }

    public boolean wasScoreUpdated() {
        return scoreUpdated;
    }

    public void updateScore(int increase) {
        lastScoreIncrease = increase;
        score += increase;
        scoreUpdated = true;
    }

    public int getLastScoreIncrease() {
        return lastScoreIncrease;
    }

    public void reset() {
        score = 0;
        lastScoreIncrease = 0;
        scoreUpdated = false;
    }
}
