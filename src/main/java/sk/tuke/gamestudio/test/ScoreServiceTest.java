package sk.tuke.gamestudio.test;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.jdbc.ScoreServiceJDBC;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreServiceTest {
    private ScoreService createService() {
        //return new ScoreServiceFile();
        return new ScoreServiceJDBC();
    }

    @Test
    public void testReset() {
        ScoreService service = createService();
        service.reset();
        assertEquals(0, service.getTopScores("testGame").size());
    }

    @Test
    public void testAddScore() {
        ScoreService service = createService();
        service.reset();
        Date date = new Date();
        service.addScore(new Score("testGame", "Jaro", 200, date));

        List<Score> scores = service.getTopScores("testGame");

        assertEquals(1, scores.size());

        assertEquals("testGame", scores.get(0).getGame());
        assertEquals("Jaro", scores.get(0).getPlayer());
        assertEquals(200, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedAt());
    }

    @Test
    public void testAddScore3() {
        ScoreService service = createService();
        service.reset();
        Date date = new Date();
        service.addScore(new Score("testGame", "Jaro", 200, date));
        service.addScore(new Score("testGame", "Fero", 400, date));
        service.addScore(new Score("testGame", "Jozo", 100, date));

        List<Score> scores = service.getTopScores("testGame");

        assertEquals(3, scores.size());

        assertEquals("testGame", scores.get(0).getGame());
        assertEquals("Fero", scores.get(0).getPlayer());
        assertEquals(400, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedAt());

        assertEquals("testGame", scores.get(1).getGame());
        assertEquals("Jaro", scores.get(1).getPlayer());
        assertEquals(200, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedAt());

        assertEquals("testGame", scores.get(2).getGame());
        assertEquals("Jozo", scores.get(2).getPlayer());
        assertEquals(100, scores.get(2).getPoints());
        assertEquals(date, scores.get(2).getPlayedAt());
    }

    @Test
    public void testAddScore10() {
        ScoreService service = createService();
        for (int i = 0; i < 20; i++)
            service.addScore(new Score("testGame", "Jaro", 200, new Date()));
        assertEquals(10, service.getTopScores("testGame").size());
    }

    @Test
    public void testPersistence() {
        ScoreService service = createService();
        service.reset();
        service.addScore(new Score("testGame", "Jaro", 200, new Date()));

        service = createService();
        assertEquals(1, service.getTopScores("testGame").size());
    }

}
