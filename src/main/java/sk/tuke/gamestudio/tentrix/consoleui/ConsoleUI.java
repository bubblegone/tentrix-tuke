package sk.tuke.gamestudio.tentrix.consoleui;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.tentrix.core.*;

import java.util.*;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private String name = "";
    private final Field field;
    private final AvailableFigures availableFigures = new AvailableFigures();
    private InputState inputState = InputState.PICK;
    private Figure selectedFigure;
    private final ScoreCounter score = new ScoreCounter();
    private final ScoreService scoreService;
    private final CommentService commentService;
    private final RatingService ratingService;

    public ConsoleUI(Field field, ScoreService scoreService,
                     CommentService commentService, RatingService ratingService) {
        this.field = field;
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }

    public void play() {
        readName();

        displayHelpMessage();
        render();
        while (field.hasSpot(availableFigures.getCurrentFigures())) {
            if (parseInput() && inputState == InputState.PICK) {
                int linesCleared = this.field.checkFullLines();
                if (linesCleared > 0) {
                    score.updateScore((int) Math.pow(linesCleared, 2));
                }
                render();
            }
        }
        System.out.println("You've lost buddy");
    }

    private void render() {
        renderField();
        if (this.score.wasScoreUpdated()) {
            System.out.printf("Score: +%d\nTotal: %d\n", this.score.getLastScoreIncrease(), score.getScore());
        }
        renderFig();
    }


    private void renderField() {
        for (int y = 9; y >= 0; y--) {
            System.out.printf("    %d ", y);
            for (int x = 0; x < 10; x++) {
                System.out.printf("%c ", field.isPositionEmpty(x, y) ? '·' : 'X');
            }
            System.out.println();
        }
        System.out.println("      0 1 2 3 4 5 6 7 8 9");
    }

    private void renderFig() {
        System.out.println("-------------------------------");
        ArrayList<Figure> currentFigures = this.availableFigures.getCurrentFigures();
        for (int y = 3; y >= 0; y--) {
            System.out.print("| ");
            for (Figure figure : currentFigures) {
                Position[] tiles = figure.getTiles();
                for (int x = 0; x < 4; x++) {
                    final int staticX = x, staticY = y;
                    boolean filled = Arrays.stream(tiles).anyMatch(t -> t.getX() == staticX && t.getY() == staticY);
                    System.out.printf("%c ", filled ? 'X' : ' ');
                }
                System.out.print("| ");
            }
            System.out.println();
        }
        System.out.println("- - - - - - - - - - - - - - - -");
        System.out.println("|   1    |     2    |    3    |");
        System.out.println("-------------------------------");
    }

    private boolean parseInput() {
        if (this.inputState == InputState.PICK) {
            System.out.print("Pick figure num[1-3]: ");
        } else {
            System.out.print("Enter coordinates:[x y]: ");
        }

        String input = this.scanner.nextLine();
        input = input.trim().replaceAll(" +", " ").toLowerCase(Locale.ROOT);

        switch (input) {
            case "q":
                System.out.println("Happy to see you next time!");
                System.exit(0);
            case "e":
                writeScore();
                System.exit(0);
                break;
            case "?":
                displayHelpMessage();
                break;
            case "r":
                renderField();
                renderFig();
            case "s":
                printScoreboard();
                break;
            case "w":
                writeScore();
                break;
            case "*":
                setRating();
                break;
            case "*m":
                printMyRating();
                break;
            case "*a":
                printAverageRating();
                break;
            case "c":
                leaveComment();
                break;
            case "ca":
                printComments(0);
                break;
            case "cl":
                printComments(10);
                break;
            case "restart":
                restartGame();
            case "scorereset":
                resetScore();
                break;
            case "commentreset":
                resetComments();
                break;
            case "ratingreset":
                resetRating();
                break;
            default:
                return handleNonSpecial(input);
        }
        return false;
    }

    private boolean handleNonSpecial(String input) {
        if (this.inputState == InputState.PICK && input.matches("^[0-9]")) {
            int pick = handlePick(input);
            if (pick == -1) {
                return false;
            }
            this.selectedFigure = availableFigures.pickFigure(pick - 1, this.selectedFigure);
            this.inputState = InputState.POS;
            return true;
        } else if (this.inputState == InputState.POS && input.matches("^[0-9] [0-9]")) {
            Position pos = handlePos(input);
            if (pos == null) {
                return false;
            }
            boolean result = this.field.insertFigure(this.selectedFigure, pos);
            if (result) {
                this.inputState = InputState.PICK;
            }
            return result;
        }
        System.out.println("Please provide correct input");
        return false;
    }

    private int handlePick(String input) {
        int figurePos;
        Scanner localScanner = new Scanner(input);
        figurePos = localScanner.nextInt();
        if (figurePos > 3 || figurePos < 1) {
            return -1;
        }
        return figurePos;
    }

    private Position handlePos(String input) {
        int x;
        int y;
        Scanner localScanner = new Scanner(input);
        x = localScanner.nextInt();
        y = localScanner.nextInt();
        if (x < 0 || x > 10 || y < 0 || y > 10) {
            return null;
        }
        return new Position(x, y);
    }

    private void displayHelpMessage() {
        System.out.print("? - bring a help message\n" +         //
                "R - show field and available figures\n" +      //
                "S - print the scoreboard\n" +                  //
                "W - write score to database\n" +               //
                "C - leave a comment\n" +                       //
                "CL - print last 10 comments\n" +               //
                "CA - print all comments\n" +                   //
                "* - set rating\n" +                            //
                "*m - print my rating\n" +                      //
                "*a - print average rating\n" +                 //
                "scorereset - delete all score records\n" +     //
                "commentreset - delete all comments\n" +        //
                "ratingreset - delete all rating records\n" +   //
                "restart - restart the game\n" +
                "E - end game, save score\n" +                  //
                "Q - instant exit without saving score\n");     //
    }

    private void printScoreboard() {
        List<Score> scoreList = scoreService.getTopScores("tentrix");
        for (Score score : scoreList) {
            System.out.printf("%s - %d\n", score.getPlayer(), score.getPoints());
        }
    }

    private void readName() {
        String name;
        while (true) {
            System.out.print("Name: ");
            name = this.scanner.nextLine();
            if (name.length() > 0) {
                break;
            }
            System.out.println("Please provide your name");
        }
        this.name = name;
    }

    private void writeScore() {
        scoreService.addScore(new Score("tentrix", name, this.score.getScore(), new Date()));
        System.out.println("Score saved");
    }

    private int readRating() {
        while (true) {
            System.out.println("Rating[* to *****]:");
            String stars = scanner.nextLine().trim();
            if (stars.matches("^(\\*){1,5}$")) {
                return stars.length();
            }
            System.out.println("Please provide correct rating using '*' character");
        }
    }

    private void setRating() {
        int rating = readRating();
        ratingService.setRating(new Rating("tentrix", name, rating, new Date()));
        System.out.println("Rating saved");
    }

    private void printStyledRating(int rating) {
        for (int i = 0; i < rating; i++) {
            System.out.print("*");
        }
        System.out.println();
    }

    private void printMyRating() {
        int rating = ratingService.getRating("tentrix", name);
        if (rating == 0) {
            System.out.print("No such player in rating database");
        }
        printStyledRating(rating);
    }

    private void printAverageRating() {
        int rating = ratingService.getAverageRating("tentrix");
        if (rating == 0) {
            System.out.println("Game wasn't rated");
        }
        printStyledRating(rating);
    }

    private String readComment() {
        while (true) {
            System.out.println("Comment[10 to 128 characters]");
            String comment = scanner.nextLine().trim();
            if (comment.length() > 10 && comment.length() < 128) {
                return comment;
            }
        }
    }

    private void leaveComment() {
        String comment = readComment();
        commentService.addComment(new Comment("tentrix", name, comment, new Date()));
        System.out.println("Comment saved");
    }

    private void printComments(int maximum) {
        List<Comment> comments = commentService.getComments("tentrix");
        if (maximum > 0) {
            maximum = Math.min(maximum, comments.size());
            comments = comments.subList(0, maximum);
        }
        for (Comment comment : comments) {
            System.out.println(comment.getPlayer());
            System.out.println(comment.getComment());
            System.out.println(comment.getCommentedOn());
            System.out.println();
        }
    }

    private void resetScore() {
        scoreService.reset();
    }

    private void resetRating() {
        ratingService.reset();
    }

    private void resetComments() {
        commentService.reset();
    }

    private void restartGame() {
        field.reset();
        score.reset();
        availableFigures.reset();
        inputState = InputState.PICK;
        System.out.println("GAME RESTARTED");
    }
}
