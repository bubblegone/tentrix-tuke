package sk.tuke.gamestudio.service.file;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreServiceFile implements ScoreService {
    private static final String SCORE_FILE = "score.bin";

    private List<Score> scores = new ArrayList<>();

    public ScoreServiceFile() {
        if (new File(SCORE_FILE).exists()) {
            load();
        } else {
            save();
        }
    }

    @Override
    public void addScore(Score score) {
        scores.add(score);
        save();
    }

    @Override
    public List<Score> getTopScores(String game) {
        load();
        return scores.stream().sorted((s1, s2) -> -Integer.compare(s1.getPoints(), s2.getPoints()))
                .limit(10).collect(Collectors.toList());
    }

    @Override
    public void reset() {
        scores = new ArrayList<>();
        save();
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORE_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            throw new GamestudioException("Error saving score ", e);
        }
    }

    private void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SCORE_FILE))) {
            scores = (List<Score>) ois.readObject();
        } catch (Exception e) {
            throw new GamestudioException("Error loading score ", e);
        }
    }
}
