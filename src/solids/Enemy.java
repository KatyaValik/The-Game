package solids;

import game.CAction;
        import game.Direction;
        import game.Position;
        import game.Size;

        import java.awt.*;
        import java.util.ArrayList;

public class Enemy extends Movable {
    private Direction verticalDir;
    private double startX;
    private double startY;
    private double height;
    private int speed;

    public Enemy(double x, double y, CAction action, SType type, double height, int speed) {
        super(x, y, action, type);
        setSize(10.1, 10.1);
        this.speed = speed;
        this.height = height;
        this.startX =  x;
        this.startY = y;
        this.verticalDir = Direction.UP;
    }

    public void moveEnemy() {
        this.setPos(this.getPos().getX(), getNextY());
    }

    public double getNextY() {
        switch (verticalDir) {
            case UP:
                if (this.getPos().getY() > this.startY - this.height) {
                    return this.getPos().getY() - speed;

                } else {
                    this.verticalDir = Direction.DOWN;
                    return this.getPos().getY() + speed;
                }
            case DOWN:
                if (this.getPos().getY() < startY) {
                    return this.getPos().getY() + speed;
                } else {
                    this.verticalDir = Direction.UP;
                    return this.getPos().getY() - speed;
                }
        }
        return this.getPos().getY();
    }

    public boolean inspectDeath (Character character) {
        double dist = Math.sqrt(Math.pow(this.getPos().getX() - character.getPos().getX(), 2) +
                Math.pow(this.getPos().getY() - character.getPos().getY(), 2));
        double r = this.getPos().getWidth() + this.getPos().getWidth();
        if (dist <= r)
            return true;
        else
            return false;
    }
}