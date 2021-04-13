package game;

import java.awt.*;
import java.awt.geom.Point2D;

public class Wall implements Solid {
    private Position pos;

    public Wall(double x, double y, double width, double height) {
        pos = new Position(x, y, width, height);
    }

    public Wall(Point2D pos, double width, double height) {
        setPos(pos.getX(), pos.getY());
        setSize(width, height);
    }

    public Point getIntPos() {
        return new Point((int) pos.getX(), (int) pos.getY());
    }

    public void setPos(double x, double y) {
        pos.setPos(x, y);
    }

    public void setSize(double width, double height) {
        pos.setSize(width, height);
    }

    public Dimension getIntSize() {
        return new Dimension((int) pos.getWidth(), (int) pos.getHeight());
    }

    public Position getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return pos.toString();
    }
}
