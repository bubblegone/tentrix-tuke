package sk.tuke.gamestudio.tentrix.core;

import java.util.ArrayList;
import java.util.Random;

public class AvailableFigures {
    private final AllFigures allFigures = new AllFigures();
    private final ArrayList<Figure> currentFigures = new ArrayList<>();
    private final Random random = new Random();

    public AvailableFigures() {
        fill();
    }

    public Figure generateFigure() {
        int figureCount = allFigures.getFigureCount();
        int figIndex = this.random.nextInt(figureCount);
        while (true) {
            Figure figure = allFigures.getFigure(figIndex);
            if (!currentFigures.contains(figure)) {
                return figure;
            }
            figIndex = this.random.nextInt(figureCount);
        }
    }

    public ArrayList<Figure> getCurrentFigures() {
        return currentFigures;
    }

    public Figure pickFigure(int position, Figure selectedFigure) {
        final Figure figure = currentFigures.get(position);
        if(selectedFigure == null){
            Figure newFigure = generateFigure();
            currentFigures.set(position, newFigure);
        }
        else{
            currentFigures.set(position, selectedFigure);
        }
        return figure;
    }

    private void fill() {
        while (currentFigures.size() < 3) {
            Figure figure = generateFigure();
            currentFigures.add(figure);
        }
    }

    public void reset() {
        currentFigures.clear();
        fill();
    }
}
