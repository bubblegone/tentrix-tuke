package sk.tuke.gamestudio.test;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.jdbc.RatingServiceJDBC;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {
    private RatingService createService() {
//        return new RatingServiceFile();
        return new RatingServiceJDBC();
    }

    @Test
    public void testReset() {
        RatingService service = createService();
        service.reset();
        assertEquals(0, service.getRating("testGame", "player"));
    }

    @Test
    public void testAddRating() {
        RatingService service = createService();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating("testGame", "player", 5, date));

        int rating = service.getRating("testGame", "player");

        assertEquals(5, rating);
    }

    @Test
    public void testAddRating3() {
        RatingService service = createService();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating("testGame", "player", 4, date));
        service.setRating(new Rating("testGame", "Fero", 3, date));
        service.setRating(new Rating("testGame", "Jozo", 2, date));

        int rating1 = service.getRating("testGame", "player");
        int rating2 = service.getRating("testGame", "Fero");
        int rating3 = service.getRating("testGame", "Jozo");

        assertEquals(4, rating1);
        assertEquals(3, rating2);
        assertEquals(2, rating3);
    }

    @Test
    public void testAddSamePlayerRating() {
        RatingService service = createService();
        service.reset();

        service.setRating(new Rating("testGame", "player", 1, new Date()));
        service.setRating(new Rating("testGame", "player", 5, new Date()));
        assertEquals(5, service.getRating("testGame", "player"));
    }

    @Test
    public void testPersistence() {
        RatingService service = createService();
        service.reset();
        service.setRating(new Rating("testGame", "player", 1, new Date()));

        service = createService();
        assertEquals(1, service.getRating("testGame", "player"));
    }

}
