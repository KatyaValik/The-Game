package solids;

        import game.CAction;
        import game.Direction;
        import game.Position;
        import game.Size;

        import java.awt.*;
        import java.util.ArrayList;

public class Enemy extends Movable {
    private Position pos = new Position(20, 20, 10, 10);
    private Direction verticalDir;
    private Size size;
    private double startX;
    private double startY;
    private double height;
    private int speed;
    private CAction action;
    private SType type;

    public Enemy(double x, double y, CAction action, SType type, double height, int speed) {
        super(x, y, action, type);
        setPos(x, y);
        this.size = new Size(10.1, 10.1);
        this.speed = speed;
        this.height = height;
        this.action = action;
        this.type = type;
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
                    return this.getPos().getY() - 1;

                } else {
                    this.verticalDir = Direction.DOWN;
                    return this.getPos().getY() + 1;
                }
            case DOWN:
                if (this.getPos().getY() < startY) {
                    return this.getPos().getY() + 1;
                } else {
                    this.verticalDir = Direction.UP;
                    return this.getPos().getY() - 1;
                }
        }
        return this.getPos().getY();
    }

    public boolean inspectDeath (Character character) {
        boolean xCross;
        boolean yCross;
        double dist = Math.sqrt(Math.pow(this.getPos().getX() - character.getPos().getX(), 2) + Math.pow(this.getPos().getY() - character.getPos().getY(), 2));
        double r = this.getPos().getWidth() + this.getPos().getWidth();
        /*if (this.getPos().getX() > character.getPos().getX()) {
            xCross = this.getPos().getX() - character.getPos().getX() < this.getPos().getWidth() + character.getPos().getWidth();
        } else {
            xCross = character.getPos().getX() - this.getPos().getX() < this.getPos().getWidth() + character.getPos().getWidth();
        }
        if (this.getPos().getY() > character.getPos().getY()) {
            yCross = this.getPos().getY() - character.getPos().getY() < this.getPos().getHeight() + character.getPos().getHeight();
        } else {
            yCross = character.getPos().getY() - this.getPos().getY() < this.getPos().getHeight() + character.getPos().getHeight();

        }
        if (xCross && yCross)
            return true;
        else
            return false;*/
        if (dist <= r)
            return true;
        else
            return false;
    }
}