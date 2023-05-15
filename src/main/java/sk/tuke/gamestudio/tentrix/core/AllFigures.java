package sk.tuke.gamestudio.tentrix.core;

import java.util.ArrayList;

public class AllFigures {
    private final ArrayList<Figure> figures = new ArrayList<>();

    public AllFigures() {
        Position pos03 = new Position(0, 3);
        Position pos02 = new Position(0, 2);
        Position pos12 = new Position(1, 2);
        Position pos01 = new Position(0, 1);
        Position pos11 = new Position(1, 1);
        Position pos21 = new Position(2, 1);
        Position pos22 = new Position(2, 2);
        Position pos00 = new Position(0, 0);
        Position pos10 = new Position(1, 0);
        Position pos20 = new Position(2, 0);
        Position pos30 = new Position(3, 0);

        Position[] dotPos = {pos00};
        Figure dot = new Figure(dotPos);
        figures.add(dot);

        Position[] cubePos = {pos00, pos10, pos01, pos11};
        Figure cube = new Figure(cubePos);
        figures.add(cube);

        Position[] bigCubePos = {pos00, pos10, pos20, pos01, pos11, pos21, pos02, pos12, pos22};
        Figure bigCube = new Figure(bigCubePos);
        figures.add(bigCube);

        Position[] vPolePos = {pos00, pos01, pos02, pos03};
        Position[] hPolePos = {pos00, pos10, pos20, pos30};
        Figure vPole = new Figure(vPolePos);
        Figure hPole = new Figure(hPolePos);
        figures.add(vPole);
        figures.add(hPole);

        Position[] gh1Pos = {pos00, pos10, pos20, pos21};
        Position[] gh2Pos = {pos01, pos11, pos21, pos20};
        Position[] gh3Pos = {pos00, pos10, pos20, pos01};
        Position[] gh4Pos = {pos01, pos11, pos21, pos00};
        Figure gh1 = new Figure(gh1Pos);
        Figure gh2 = new Figure(gh2Pos);
        Figure gh3 = new Figure(gh3Pos);
        Figure gh4 = new Figure(gh4Pos);
        figures.add(gh1);
        figures.add(gh2);
        figures.add(gh3);
        figures.add(gh4);

        Position[] gv1Pos = {pos00, pos01, pos02, pos12};
        Position[] gv2Pos = {pos10, pos11, pos12, pos02};
        Position[] gv3Pos = {pos00, pos01, pos02, pos10};
        Position[] gv4Pos = {pos10, pos11, pos12, pos00};
        Figure gv1 = new Figure(gv1Pos);
        Figure gv2 = new Figure(gv2Pos);
        Figure gv3 = new Figure(gv3Pos);
        Figure gv4 = new Figure(gv4Pos);
        figures.add(gv1);
        figures.add(gv2);
        figures.add(gv3);
        figures.add(gv4);

        Position[] p1Pos = {pos00, pos10, pos20, pos11};
        Position[] p2Pos = {pos01, pos11, pos21, pos11};
        Position[] p3Pos = {pos00, pos01, pos02, pos11};
        Position[] p4Pos = {pos10, pos11, pos12, pos10};
        Figure p1 = new Figure(p1Pos);
        Figure p2 = new Figure(p2Pos);
        Figure p3 = new Figure(p3Pos);
        Figure p4 = new Figure(p4Pos);
        figures.add(p1);
        figures.add(p2);
        figures.add(p3);
        figures.add(p4);

        Position[] z1Pos = {pos00, pos10, pos11, pos21};
        Position[] z2Pos = {pos10, pos11, pos01, pos02};
        Position[] z3Pos = {pos01, pos11, pos10, pos20};
        Position[] z4Pos = {pos00, pos01, pos11, pos12};
        Figure z1 = new Figure(z1Pos);
        Figure z2 = new Figure(z2Pos);
        Figure z3 = new Figure(z3Pos);
        Figure z4 = new Figure(z4Pos);
        figures.add(z1);
        figures.add(z2);
        figures.add(z3);
        figures.add(z4);

        Position[] c1Pos = {pos00, pos01, pos11};
        Position[] c2Pos = {pos10, pos11, pos11};
        Position[] c3Pos = {pos00, pos01, pos10};
        Position[] c4Pos = {pos10, pos11, pos00};
        Figure c1 = new Figure(c1Pos);
        Figure c2 = new Figure(c2Pos);
        Figure c3 = new Figure(c3Pos);
        Figure c4 = new Figure(c4Pos);
        figures.add(c1);
        figures.add(c2);
        figures.add(c3);
        figures.add(c4);

    }

    public int getFigureCount() {
        return figures.size();
    }

    public Figure getFigure(int index) {
        return figures.get(index);
    }

}
