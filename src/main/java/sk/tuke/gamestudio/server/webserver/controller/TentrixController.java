package sk.tuke.gamestudio.server.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.RedirectView;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.server.auth.AuthServiceJPA;
import sk.tuke.gamestudio.server.auth.Player;
import sk.tuke.gamestudio.service.jpa.ScoreServiceJPA;
import sk.tuke.gamestudio.tentrix.core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Controller
@RequestMapping("/tentrix")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class TentrixController {
    private final Field field = new Field();
    private InputState inputState = InputState.PICK;
    private final AvailableFigures availableFigures = new AvailableFigures();
    private Figure selectedFigure;
    private final ScoreCounter score = new ScoreCounter();

    @Autowired
    private AuthServiceJPA authServiceJPA;
    @Autowired
    private ScoreServiceJPA scoreServiceJPA;

    private boolean isAuthed = false;
    private String userName;


    @RequestMapping
    public String tentrix(@RequestParam(required = false) Integer pick,
                          @RequestParam(required = false) Integer x,
                          @RequestParam(required = false) Integer y,
                          @RequestParam(required = false) Integer pickPos
    ) {

        if(!isAuthed){
            return "auth";
        }
        if (pick == null) {
            this.inputState = InputState.PICK;
        }
        else if (pick == 1) {
            boolean result = actionPick(pickPos);
            if (result) {
                this.inputState = InputState.POS;
            }
        } else {
            boolean result = actionPos(x, y);
            if (result) {
                this.inputState = InputState.PICK;
            }
            if(!field.hasSpot(this.availableFigures.getCurrentFigures()) && this.inputState != InputState.FINISHED){
                endGame();
            }
        }
        return "tentrix";
    }

    @RequestMapping("/new")
    private RedirectView restartGame() {
        this.field.reset();
        this.availableFigures.reset();
        this.score.reset();
        this.selectedFigure = null;
        this.inputState = InputState.PICK;
        return new RedirectView("/tentrix");
    }

    @RequestMapping("/logout")
    private RedirectView logout() {
        this.isAuthed = false;
        this.userName = "";
        return restartGame();
    }

    @PostMapping("/auth/check")
    public ResponseEntity<String> validateUser(@RequestBody Player user){
        System.out.println("CHECK");
        user.hashPassword();
        this.isAuthed = authServiceJPA.verifyUser(user);
        if(isAuthed){
            System.out.println("AUTHED");
            this.userName = user.getName();
            return new ResponseEntity<>("Logged in",
                   HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Credentials don't match any user!",
                   HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/create")
    public ResponseEntity<String> createUser(@RequestBody Player user){
        System.out.println("CREATE");
        user.hashPassword();
        this.isAuthed = authServiceJPA.createUser(user);
        if(isAuthed){
            System.out.println("AUTHED");
            this.userName = user.getName();
            return new ResponseEntity<>("User created",
                    HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("User already exists!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void endGame(){
        this.inputState = InputState.FINISHED;
        this.scoreServiceJPA.addScore(new Score("tentrix", this.userName, this.score.getScore(), new Date()));
    }

    private boolean actionPos(int x, int y) {
        if (x < 0 || x > 10 || y < 0 || y > 10) {
            return false;
        }
        Position position = new Position(x, y);
        boolean result =  this.field.insertFigure(this.selectedFigure, position);
        if(result){
            int linesCleared = this.field.checkFullLines();
            if (linesCleared > 0) {
                score.updateScore((int) Math.pow(linesCleared, 2));
            }
            this.selectedFigure = null;
            return true;
        }
        return false;
    }

    private boolean actionPick(Integer pos) {
        if(pos > 2 || pos < 0){
            return false;
        }

        this.selectedFigure = availableFigures.pickFigure(pos, this.selectedFigure);
        return true;
    }

    private String getFieldCell(boolean isEmpty, int x, int y) {
        String imgName = isEmpty ? "bg-white" : "bg-red";
        boolean hasURL = this.inputState == InputState.POS;
        String url = hasURL ? "<a href='/tentrix?x=%d&y=%d&pick=0'/>".formatted(x, y) : "";
        return "<td class='%s'>".formatted(imgName) +
                url +
                "</td>\n";

    }

    private void appendTopLine(StringBuilder sb) {
        sb.append("<tr>\n");
        sb.append("<td>X</td>\n");
        for (int i = 0; i <= 9; i++) {
            sb.append("<td>%d</td>\n".formatted(i));
        }
        sb.append("<td>X</td>\n");
        sb.append("</tr>\n");
    }

    public String getHTMLField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        appendTopLine(sb);
        for (int y = 9; y >= 0; y--) {
            sb.append("<tr>\n");
            sb.append("<td>%d</td>\n".formatted(y));
            for (int x = 0; x < 10; x++) {
                boolean isEmpty = field.isPositionEmpty(x, y);
                sb.append(getFieldCell(isEmpty, x, y));
            }
            sb.append("<td>%d</td>\n".formatted(y));
            sb.append("</tr>\n");
        }
        appendTopLine(sb);
        sb.append("</table>\n");
        return sb.toString();
    }

    private String getPickCell(boolean isFilled, int pickPos, boolean isFirst) {
        String imgName = isFirst ? "bg-red" : isFilled ? "bg-black" : "bg-white";
        boolean hasURL = pickPos != -1 && inputState != InputState.FINISHED;
        String url = hasURL ? "<a href='/tentrix?pickPos=%d&pick=1'/>".formatted(pickPos) : "";
        return "<td class='%s'>".formatted(imgName) +
                url +
                "</td>\n";
    }

    private Position getLeftDownTile(Position[] figureTiles) {
        Position leftmostDown = figureTiles[0];
        for (Position tile : figureTiles) {
            if (tile.getX() < leftmostDown.getX()) {
                leftmostDown = tile;
            } else if (tile.getX() == leftmostDown.getX() && leftmostDown.getY() > tile.getY()) {
                leftmostDown = tile;

            }
        }
        return leftmostDown;
    }

    private String getHTMLFigure(Position[] tiles, int position){
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        Position leftDownTile = getLeftDownTile(tiles);
        for (int y = 3; y >= 0; y--) {
            sb.append("<tr>\n");
            for (int x = 0; x < 4; x++) {
                if (leftDownTile.getY() == y && leftDownTile.getX() == x) {
                    sb.append(getPickCell(true, position, true));
                } else {
                    final int staticX = x, staticY = y;
                    boolean isFilled = Arrays.stream(tiles).anyMatch(t -> t.getX() == staticX && t.getY() == staticY);
                    sb.append(getPickCell(isFilled, position, false));
                }
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    public String getHTMLPick() {
        StringBuilder sb = new StringBuilder();

        ArrayList<Figure> currentFigures = this.availableFigures.getCurrentFigures();
        for (int i = 0; i < 3; i++) {
            Position[] tiles = currentFigures.get(i).getTiles();
            sb.append(getHTMLFigure(tiles, i));
        }
        return sb.toString();
    }

    public String getHTMLScore(){
        if(this.inputState != InputState.FINISHED){
            return "<h1 id='score'>SCORE: [ %d ]</h1>".formatted(this.score.getScore());
        }
        else{
            return "<h1 id='finished-score'>GAME FINISHED: [ %d ]</h1>".formatted(this.score.getScore());
        }
    }

    public String getHTMLCurrentFigure(){
        if(this.selectedFigure == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        sb.append("<h3>Current figure:</h3>");
        Position[] tiles = this.selectedFigure.getTiles();

        sb.append(getHTMLFigure(tiles, -1));
        sb.append("</div>");
        return sb.toString();
    }

    public String getHTMLState(){
        return this.inputState == InputState.FINISHED ?
                "No action possible" :
                this.inputState == InputState.POS ?
                        "Pick position for figure" :
                        "Pick Figure";
    }

    public String getUser(){
        return "User: " + this.userName;
    }
}
