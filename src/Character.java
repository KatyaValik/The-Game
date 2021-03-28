import java.awt.*;

public class Character {
    private double x;
    private double y;
    private double verticalSpeed;
    private double instantVertAcc = -2.7;
    private double instantHorAcc = 2;
    private Direction verticalDir = Direction.NO;

    public Character() {
        x = 20;
        y = 20;
    }

    public Character(double x, double y) {
        setPos(x, y);
    }

    public void moveHor(Direction dir) {
        x += dir.getValue() * instantHorAcc;
    }

    public void moveVert(Direction dir, double globalAcc, double border) {
        switch (dir) {
            case FORWARD:
                if (verticalDir != Direction.BACK) {
                    verticalDir = dir;
                    verticalSpeed = instantVertAcc;
                    y += verticalSpeed;
                }
                break;
            case BACK:
                if (verticalDir == Direction.NO)
                    verticalDir = dir;
                verticalSpeed += globalAcc;
                if (verticalDir == Direction.BACK) {
                    y = Math.min(verticalSpeed + y, border);
                    if (y == border) {
                        verticalSpeed = 0;
                        verticalDir = Direction.NO;
                    }
                }
                else y += verticalSpeed;
                break;
            case NO:
                verticalDir = dir;
                verticalSpeed = 0;
                break;
        }
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point getPos() {
        return new Point(Math.toIntExact(Math.round(Math.floor(x))), Math.toIntExact(Math.round(Math.floor(y))));
    }

//    public Direction getVertDir() {
//        return verticalDir;
//    }
}
