package sk.tuke.gamestudio.test;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.file.CommentServiceFile;
import sk.tuke.gamestudio.service.jdbc.CommentServiceJDBC;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentServiceTest {
    private CommentService createService() {
        return new CommentServiceFile();
//        return new CommentServiceJDBC();
    }

    @Test
    public void testReset() {
        CommentService service = createService();
        service.reset();
        assertEquals(0, service.getComments("testGame").size());
    }

    @Test
    public void testAddComment() {
        CommentService service = createService();
        service.reset();
        Date date = new Date();
        service.addComment(new Comment("testGame", "Jaro", "good game", date));

        List<Comment> comments = service.getComments("testGame");

        assertEquals(1, comments.size());

        assertEquals("testGame", comments.get(0).getGame());
        assertEquals("Jaro", comments.get(0).getPlayer());
        assertEquals("good game", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
    }

    @Test
    public void testAddComment3() {
        CommentService service = createService();
        service.reset();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.JANUARY, 1);
        Date date = calendar.getTime();
        calendar.set(2019, Calendar.JANUARY, 1);
        Date date2 = calendar.getTime();
        calendar.set(2021, Calendar.JANUARY, 1);
        Date date3 = calendar.getTime();

        service.addComment(new Comment("testGame", "Jaro", "first comment", date));
        service.addComment(new Comment("testGame", "Fero", "second comment", date2));
        service.addComment(new Comment("testGame", "Jozo", "third comment", date3));

        List<Comment> comments = service.getComments("testGame");

        assertEquals(3, comments.size());

        assertEquals("testGame", comments.get(1).getGame());
        assertEquals("Jaro", comments.get(1).getPlayer());
        assertEquals("first comment", comments.get(1).getComment());
        assertEquals(date, comments.get(1).getCommentedOn());

        assertEquals("testGame", comments.get(2).getGame());
        assertEquals("Fero", comments.get(2).getPlayer());
        assertEquals("second comment", comments.get(2).getComment());
        assertEquals(date2, comments.get(2).getCommentedOn());

        assertEquals("testGame", comments.get(0).getGame());
        assertEquals("Jozo", comments.get(0).getPlayer());
        assertEquals("third comment", comments.get(0).getComment());
        assertEquals(date3, comments.get(0).getCommentedOn());
    }

    @Test
    public void testPersistence() {
        CommentService service = createService();
        service.reset();
        service.addComment(new Comment("testGame", "Jaro", "comment", new Date()));

        service = createService();
        assertEquals(1, service.getComments("testGame").size());
    }

}
