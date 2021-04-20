package solids;

import game.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Stone implements Solid {
    private Position pos;
    private CAction action;

    public Stone(double x, double y, double width, double height) {
        pos = new Position(x, y, width, height);
        action = CAction.NUDGE;
    }

    public Stone(double x, double y, double width, double height, CAction action) {
        pos = new Position(x, y, width, height);
        this.action = action;
    }


    private double verticalSpeed;
    private double instantVertAcc = -2.7;
    private double horizontalSpeed = 0;
    private double instantHorAcc = 2;
    private Direction verticalDir = Direction.NO;
    private CState state = CState.NORMAL;
    private Solid triggeredSolid;

    public CState getState() {
        return state;
    }

    public void normalizeState() {
        state = CState.NORMAL;
    }

    public Solid getTriggeredSolid() {
        return triggeredSolid;
    }

    public void revive(double x, double y) {
        state = CState.NORMAL;
        setPos(x, y);
    }

    public Stone(Point2D pos) {
        setPos(pos);
    }

    public Stone(double x, double y) {
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
        var action = (ray == null) ? CAction.NUDGE : ray.getSolid().getAction();
        if (Math.abs(rayLength) < Math.abs(instantHorAcc)) {
            triggeredSolid = ray.getSolid();
            switch (action) {
                case NUDGE -> {
                    return pos.getX() + rayLength;
                }
                case KILL -> state = CState.IS_DYING;
                case RELOCATE -> state = CState.WAITING_TRANSITION;
                case SPAWN -> state = CState.TOUCHED_SPAWNER;
                default -> {
                    return 1;
                }
            }
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
        var action = (ray == null) ? CAction.NUDGE : ray.getSolid().getAction();
        if (Math.abs(rayLength) < Math.abs(verticalSpeed) && verticalSpeed < 0) {
            triggeredSolid = ray.getSolid();
            switch (action) {
                case NUDGE -> {
                    verticalDir = Direction.BACK;
                    verticalSpeed = 0;
                    return pos.getY() + rayLength;
                }
                case KILL -> state = CState.IS_DYING;
                case RELOCATE -> state = CState.WAITING_TRANSITION;
                default -> {
                    return 12;
                }
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
        var action = (ray == null) ? CAction.NUDGE : ray.getSolid().getAction();
        if (rayLength < verticalSpeed) {
            triggeredSolid = ray.getSolid();
            switch (action) {
                case NUDGE -> verticalDir = Direction.NO;
                case KILL -> state = CState.IS_DYING;
                case RELOCATE -> state = CState.WAITING_TRANSITION;
                default -> {
                    return 12;
                }
            }
        }
        return pos.getY() + Math.min(rayLength, verticalSpeed);
    }

    public double getNextYNo(Direction dir, double globalAcc, ArrayList<Solid> solids) {
        var ray = getClosestPlatform(solids);
        var rayLength = (ray == null) ? Double.MAX_VALUE : ray.getLength(false, true);
        var action = (ray == null) ? CAction.NUDGE : ray.getSolid().getAction();
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
                    var actionUp = (rayUp == null) ? CAction.NUDGE : rayUp.getSolid().getAction();
                    verticalSpeed = instantVertAcc;
                    if (Math.abs(rayUpLength) < Math.abs(verticalSpeed)) {
                        switch (actionUp) {
                            case NUDGE:
                                verticalDir = Direction.BACK;
                                verticalSpeed = 0;
                                return pos.getY() + rayUpLength;
                            case KILL:
                                state = CState.IS_DYING;
                                return 0;
                            case RELOCATE:
                                state = CState.WAITING_TRANSITION;
                                return 0;
                            default:
                                return 12;
                        }
                    } else verticalDir = Direction.FORWARD;

                    return pos.getY() + verticalSpeed;
                case KILL:
                    state = CState.IS_DYING;
                    return 0;
                case RELOCATE:
                    state = CState.WAITING_TRANSITION;
                    return 0;
                default:
                    return 12;
            }
        } else {
            switch (action) {
                case NUDGE:
                    verticalSpeed = 0;
                    return pos.getY();
                case KILL:
                    state = CState.IS_DYING;
                    return 0;
                case RELOCATE:
                    state = CState.WAITING_TRANSITION;
                    return 0;
                default:
                    return 12;
            }
        }
    }
//endregion getNextY

    //region getClosestSolid
    public Ray getClosestCeil(ArrayList<Solid> solids) {
        Ray min = null;
        for (var solid : solids) {
            if (solid.getAction() != CAction.PASS && solid.getAction() != CAction.SPAWN) {
                var ray = new Ray(pos, solid.getPos(), solid);
                var length = ray.getLength(false, true);
                if (Math.abs(length) < Double.MAX_VALUE && ray.endIsUponStart() &&
                        (min == null || Math.abs(min.getLength(false, true)) > Math.abs(length))) {
                    min = ray;
                }
            }
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
            if (solid.getAction() != CAction.PASS) {
                var ray = new Ray(pos, solid.getPos(), solid);
                var length = ray.getLength(true, false);
                if (Math.abs(length) < Double.MAX_VALUE && ray.endIsRighterThanStart() &&
                        (min == null || Math.abs(min.getLength(true, false)) > Math.abs(length))) {
                    min = ray;
                }
            }
        }
        return min;
    }

    public Ray getClosestLeft(ArrayList<Solid> solids) {
        Ray min = null;
        for (var solid : solids) {
            if (solid.getAction() != CAction.PASS) {
                var ray = new Ray(pos, solid.getPos(), solid);
                var length = ray.getLength(true, false);
                if (Math.abs(length) < Double.MAX_VALUE && ray.endIsLefterThanStart() &&
                        (min == null || Math.abs(min.getLength(true, false)) > Math.abs(length))) {
                    {
                        min = ray;
                    }
                }
            }
        }
        return min;
    }

    public Ray getClosestPlatform(ArrayList<Solid> solids) {
        Ray min = null;
        for (var solid : solids) {
            if (solid.getAction() != CAction.PASS && solid.getAction() != CAction.SPAWN) {
                var ray = new Ray(pos, solid.getPos(), solid);
                var length = ray.getLength(false, true);
                if (Math.abs(length) < Double.MAX_VALUE && ray.endIsUnderStart() &&
                        (min == null || min.getLength(false, true) > length)) {
                    min = ray;
                }
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

    @Override
    public SType getType() {
        return null;
    }
    //endregion Actions With Pos and Size


    @Override
    public String toString() {
        return pos.toString();
    }
}
