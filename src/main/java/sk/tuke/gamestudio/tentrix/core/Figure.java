package sk.tuke.gamestudio.tentrix.core;

public class Figure {
    private final Position[] tiles;

    public Figure(Position[] tiles) {
        this.tiles = tiles;
    }

    public Position[] getTiles() {
        return tiles;
    }
}
