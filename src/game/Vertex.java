package game;

import java.awt.geom.Point2D;

public class Vertex extends Point2D {
    private double x;
    private double y;

    public Vertex(double x, double y) {
        setLocation(x, y);
    }

    public Vertex getMoved(double xOffset, double yOffset) {
        return new Vertex(getX() + xOffset, getY() + yOffset);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("x: %f, y: %f", x, y);
    }
}
