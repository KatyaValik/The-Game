package game;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Position {
    private double x;
    private double y;
    private Size size;

    public Position(double x, double y, double width, double height) {
        setPos(x, y);
        size = new Size(width, height);
    }

    public Position(Point2D point, Size size) {
        setPos(point.getX(), point.getY());
        this.size = size;
    }

    //region actions with Pos and Size
    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setPos(Point2D pos) {
        x = pos.getX();
        y = pos.getY();
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setSize(double width, double height) {
        size.setSize(width, height);
    }

    public Vertex getPos() {
        return new Vertex(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Size getSize() {
        return size;
    }

    public double getWidth() {
        return size.getWidth();
    }

    public double getHeight() {
        return size.getHeight();
    }
    //endregion actions with Pos and Size

    //region getSides
    public Line2D getLeft() {
        return new Line2D.Double(getPos(), getPos().getMoved(0, getHeight()));
    }

    public Line2D getRight() {
        return new Line2D.Double(getPos().getMoved(getWidth(), 0), getPos().getMoved(getWidth(), getHeight()));
    }

    public Line2D getTop() {
        return new Line2D.Double(getPos(), getPos().getMoved(getWidth(), 0));
    }

    public Line2D getBottom() {
        return new Line2D.Double(getPos().getMoved(0, getHeight()), getPos().getMoved(getWidth(), getHeight()));
    }
    //endregion getSides

    public Vertex getMoved(double xOffset, double yOffset) {
        return getPos().getMoved(xOffset, yOffset);
    }

    @Override
    public String toString() {
        return String.format("x: %f, y: %f", x, y);
    }
}
