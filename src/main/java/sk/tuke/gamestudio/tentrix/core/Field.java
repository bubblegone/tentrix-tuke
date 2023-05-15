package sk.tuke.gamestudio.tentrix.core;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private final TileState[][] tiles = new TileState[10][10];

    public Field() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.tiles[i][j] = TileState.EMPTY;
            }
        }
    }

    public boolean isPositionEmpty(int x, int y) {
        return this.tiles[y][x] == TileState.EMPTY;
    }

    public boolean insertFigure(Figure figure, Position position) {
        Position[] figureTiles = figure.getTiles().clone();
        Position leftmostDown = figure.getTiles()[0];
        for (Position tile : figureTiles) {
            if (tile.getX() < leftmostDown.getX()) {
                leftmostDown = tile;
            } else if (tile.getX() == leftmostDown.getX() && leftmostDown.getY() > tile.getY()) {
                leftmostDown = tile;
            }
        }

        int xAdd = position.getX() - leftmostDown.getX();
        int yAdd = position.getY() - leftmostDown.getY();

        for (Position tile : figureTiles) {
            int tileX = tile.getX() + xAdd;
            int tileY = tile.getY() + yAdd;
            if (tileY > 9 || tileX > 9 || tileY < 0 || tileX < 0) {
                return false;
            }
            if (this.tiles[tileY][tileX] != TileState.EMPTY) {
                return false;
            }
        }
        for (Position tile : figureTiles) {
            this.tiles[tile.getY() + yAdd][tile.getX() + xAdd] = TileState.FILLED;
        }
        return true;
    }

    private boolean checkRow(int row) {
        for (int i = 0; i < 10; i++) {
            if (tiles[row][i] == TileState.EMPTY) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int column) {
        for (int i = 0; i < 10; i++) {
            if (tiles[i][column] == TileState.EMPTY) {
                return false;
            }
        }
        return true;
    }

    public int checkFullLines() {
        ArrayList<Integer> rowsToBeCleared = new ArrayList<>();
        ArrayList<Integer> colsToBeCleared = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (checkRow(i)) {
                rowsToBeCleared.add(i);
            }
            if (checkColumn(i)) {
                colsToBeCleared.add(i);
            }
        }

        for (int row : rowsToBeCleared) {
            for (int i = 0; i < 10; i++) {
                tiles[row][i] = TileState.EMPTY;
            }
        }
        for (int col : colsToBeCleared) {
            for (int i = 0; i < 10; i++) {
                tiles[i][col] = TileState.EMPTY;
            }
        }

        return rowsToBeCleared.size() + colsToBeCleared.size();
    }

    public boolean hasSpot(List<Figure> figures) {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (tiles[y][x] == TileState.EMPTY) {
                    for (Figure figure : figures) {
                        boolean possible = true;
                        for (Position tile : figure.getTiles()) {
                            if (y + tile.getY() < 10 && x + tile.getX() < 10) {
                                if (tiles[y + tile.getY()][x + tile.getX()] != TileState.EMPTY) {
                                    possible = false;
                                    break;
                                }
                            } else {
                                possible = false;
                                break;
                            }

                        }
                        if (possible) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void reset() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tiles[i][j] = TileState.EMPTY;
            }
        }
    }
}
