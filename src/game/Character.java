package game;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Character implements Solid {
    private Position pos = new Position(20, 20, 10, 10);
    private double verticalSpeed;
    private double instantVertAcc = -2.7;
    private double horizontalSpeed = 0;
    private double instantHorAcc = 2;
    private Direction verticalDir = Direction.NO;

    public Character(Point2D pos) {
        setPos(pos);
    }

    public Character(double x, double y) {
        setPos(x, y);
        setSize(10, 10);
    }

    public void move(Direction hor, Direction vert, double globalAcc, ArrayList<Solid> solids) {
        var nextX = getNextX(hor, solids);
        var nextY = getNextY(vert, globalAcc, solids);

        setPos(nextX, nextY);
    }

    public double getNextX(Direction dir, ArrayList<Solid> solids) {
        var ray = getClosestHor(solids, dir);
        var rayLength = (ray == null) ? Double.MAX_VALUE : ray.getLength(true, false);
        var action = (ray == null) ? CAction.NUDGE : ray.getAction();
        if (Math.abs(rayLength) < Math.abs(instantHorAcc)) {
            return switch (action) {
                case NUDGE -> pos.getX() + rayLength;
                default -> 1;
            };
        }
        return pos.getX() + dir.getValue() * instantHorAcc;
    }

    //region getNextY
    public double getNextY(Direction dir, double globalAcc, ArrayList<Solid> solids) {
        return switch (verticalDir) {
            case FORWARD -> getNextYForward(globalAcc, solids);
            case BACK -> getNextYBack(globalAcc, solids);
            case NO -> getNextYNo(dir, globalAcc, solids);
            default -> pos.getY();
        };
    }

    public double getNextYForward(double globalAcc, ArrayList<Solid> solids) {
        var ray = getClosestCeil(solids);
        verticalSpeed += globalAcc;
        double rayLength = (ray == null) ? Double.MAX_VALUE : ray.getLength(false, true);
        var action = (ray == null) ? CAction.NUDGE : ray.getAction();
        if (Math.abs(rayLength) < Math.abs(verticalSpeed) && verticalSpeed < 0) {
            switch (action) {
                case NUDGE:
                    verticalDir = Direction.BACK;
                    verticalSpeed = 0;
                    return pos.getY() + rayLength;
                default:
                    return 12;
            }
        }
        if (verticalSpeed > 0)
            verticalDir = Direction.BACK;
        return pos.getY() + verticalSpeed;
    }

    public double getNextYBack(double globalAcc, ArrayList<Solid> solids) {
        verticalSpeed += globalAcc;
        var ray = getClosestPlatform(solids);
        double rayLength = (ray == null) ? Double.MAX_VALUE : ray.getLength(false, true);
        var action = (ray == null) ? CAction.NUDGE : ray.getAction();
        if (rayLength < verticalSpeed) {
            switch (action) {
                case NUDGE:
                    verticalDir = Direction.NO;
                    return pos.getY() + rayLength;
                default: return 12;
            }
        }
        return pos.getY() + Math.min(rayLength, verticalSpeed);
    }

    public double getNextYNo(Direction dir, double globalAcc, ArrayList<Solid> solids) {
        var ray = getClosestPlatform(solids);
        var rayLength = (ray == null) ? Double.MAX_VALUE : ray.getLength(false, true);
        var action = (ray == null) ? CAction.NUDGE : ray.getAction();
        if (rayLength > 0) {
            verticalSpeed += globalAcc;
            if (verticalSpeed < rayLength)
                verticalDir = Direction.BACK;
            return pos.getY() + Math.min(rayLength, verticalSpeed);
        } else if (dir == Direction.FORWARD) {
            switch (action) {
                case NUDGE:
                    var rayUp = getClosestCeil(solids);
                    var rayUpLength = (rayUp == null) ? Double.MAX_VALUE : rayUp.getLength(false, true);
                    var actionUp = (rayUp == null) ? CAction.NUDGE : rayUp.getAction();
                    verticalSpeed = instantVertAcc;
                    if (Math.abs(rayUpLength) < Math.abs(verticalSpeed)) {
                        switch (actionUp) {
                            case NUDGE:
                                verticalDir = Direction.BACK;
                                verticalSpeed = 0;
                                return pos.getY() + rayUpLength;
                            default: return 12;
                        }
                    } else verticalDir = Direction.FORWARD;

                    return pos.getY() + verticalSpeed;
                default: return 12;
            }
        } else {
            switch (action) {
                case NUDGE:
                    verticalSpeed = 0;
                    return pos.getY();
                default: return 12;
            }
        }
    }
//endregion getNextY

    //region getClosestSolid
    public Ray getClosestCeil(ArrayList<Solid> solids) {
        Ray min = null;
        for (var solid : solids) {
            var ray = new Ray(pos, solid.getPos(), solid.getAction());
            if (ray.endIsExactlyUponStart())
                return ray;
            if (ray.endIsUponStart() &&
                    (min == null || Math.abs(min.getLength(false, true)) > Math.abs(ray.getLength(false, true))))
                min = ray;
        }
        return min;
    }

    public Ray getClosestHor(ArrayList<Solid> solids, Direction dir) {
        return switch (dir) {
            case FORWARD -> getClosestRight(solids);
            case BACK -> getClosestLeft(solids);
            default -> null;
        };
    }

    public Ray getClosestRight(ArrayList<Solid> solids) {
        Ray min = null;
        for (var solid : solids) {
            var ray = new Ray(pos, solid.getPos(), solid.getAction());
            if (ray.endIsExactlyRighterThanStart())
                return ray;
            if (ray.endIsRighterThanStart() &&
                    (min == null || Math.abs(min.getLength(true, false)) > Math.abs(ray.getLength(true, false))))
                min = ray;
        }
        return min;
    }

    public Ray getClosestLeft(ArrayList<Solid> solids) {
        Ray min = null;
        for (var solid : solids) {
            var ray = new Ray(pos, solid.getPos(), solid.getAction());
            if (ray.endIsExactlyLefterThatStart())
                return ray;
            if (ray.endIsLefterThanStart() &&
                    (min == null || Math.abs(min.getLength(true, false)) > Math.abs(ray.getLength(true, false))))
                min = ray;
        }
        return min;
    }

    public Ray getClosestPlatform(ArrayList<Solid> solids) {
        Ray min = null;
        for (var solid : solids) {
            var ray = new Ray(pos, solid.getPos(), solid.getAction());
            if (ray.endIsExactlyUnderStart())
                return ray;
            if (ray.endIsUnderStart() &&
                    (min == null || min.getLength(false, true) > ray.getLength(false, true))) {
                min = ray;
            }
        }
        return min;
    }
    //endregion getClosestSolid

    //region actions With Pos and Size
    public void setPos(double x, double y) {
        this.pos.setPos(x, y);
    }

    public void setPos(Point2D pos) {
        this.pos.setPos(pos);
    }

    public void setSize(double width, double height) {
        this.pos.setSize(width, height);
    }

    public Point getIntPos() {
        return new Point((int) pos.getPos().getX(), (int) pos.getPos().getY());
    }

    public Dimension getIntSize() {
        return new Dimension((int) pos.getSize().getWidth(), (int) pos.getSize().getHeight());
    }

    public Position getPos() {
        return pos;
    }

    @Override
    public CAction getAction() {
        return null;
    }
    //endregion Actions With Pos and Size
}
