package sk.tuke.gamestudio.test;

public class Main {
    public static void main(String[] args) {
        testScore();
        testComment();
        testRating();
    }

    private static void testScore() {
        ScoreServiceTest scoreServiceTest = new ScoreServiceTest();
        scoreServiceTest.testReset();
        scoreServiceTest.testAddScore();
        scoreServiceTest.testAddScore3();
        scoreServiceTest.testAddScore10();
        scoreServiceTest.testPersistence();
    }

    private static void testComment() {
        CommentServiceTest commentServiceTest = new CommentServiceTest();
        commentServiceTest.testReset();
        commentServiceTest.testAddComment();
        commentServiceTest.testAddComment3();
        commentServiceTest.testPersistence();
    }

    private static void testRating() {
        RatingServiceTest ratingServiceTest = new RatingServiceTest();
        ratingServiceTest.testReset();
        ratingServiceTest.testAddRating();
        ratingServiceTest.testAddRating3();
        ratingServiceTest.testAddSamePlayerRating();
        ratingServiceTest.testPersistence();
    }
}
