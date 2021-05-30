package solids;

import game.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Movable implements Solid {
    private Position pos = new Position(20, 20, 10, 10);
    private double verticalSpeed;
    private double instantVertAcc = -2.7;
    private double horizontalSpeed = 0;
    private double instantHorAcc = 2;
    private Direction verticalDir = Direction.NO;
    private CState state = CState.IS_DYING;
    private Solid triggeredSolid;
    private SType type;
    private CAction action;

    public CState getState() {
        return state;
    }

    public void normalizeState() {
        state = CState.NORMAL;
        triggeredSolid = null;
    }

    public Solid getTriggeredSolid() {
        return triggeredSolid;
    }

    public void fastTeleport(double x, double y) {
        normalizeState();
        setPos(x, y);
    }

    public Movable(double x, double y, CAction action, SType type) {
        setPos(x, y);
        setSize(10, 10);
        this.action = action;
        this.type = type;
    }

    public void move(Direction hor, Direction vert, double globalAcc, ArrayList<Solid> solids) {
        var nextX = getNextX(hor, solids);
        if (state == CState.WAITING_TRANSITION)
            return;
        var nextY = getNextY(vert, globalAcc, solids);

        setPos(nextX, nextY);
    }

    public Boolean isTouchingCharacter(Direction dir, ArrayList<Solid> solids) {
        var ray = getClosestHor(solids, dir);
        var rayLength = (ray == null) ? Double.MAX_VALUE : ray.getLength(true, false);
        if (Math.abs(rayLength) < Math.abs(instantHorAcc)) {
            return ray.getSolid().getType() == SType.CHARACTER;
        }
        return false;
    }

    public void inspectCollision(ArrayList<Solid> solids) {
        for (Solid solid : solids) {
            if (Math.max(pos.getX(), solid.getPos().getX()) <
                    Math.min(pos.getRight().getX2(), solid.getPos().getRight().getX2()) &&
                    Math.max(pos.getTop().getY1(), solid.getPos().getTop().getY1()) <
                            Math.min(pos.getBottom().getY1(), solid.getPos().getBottom().getY1())) {
                switch (solid.getAction()) {
                    case SWITCH -> {
                        state = CState.TOUCHED_SWITCH;
                        triggeredSolid = solid;
                    }
                }
            }
        }
    }

    public double getNextX(Direction dir, ArrayList<Solid> solids) {
        var ray = getClosestHor(solids, dir);
        var rayLength = (ray == null) ? Double.MAX_VALUE : ray.getLength(true, false);
        var action = (ray == null) ? CAction.NUDGE : ray.getSolid().getAction();
        if (Math.abs(rayLength) < Math.abs(instantHorAcc)) {
            triggeredSolid = ray.getSolid();
            switch (action) {
                case KILL -> {
                    state = CState.IS_DYING;
                    return pos.getX() + rayLength;
                }
                case NUDGE -> {
                    return pos.getX() + rayLength;
                }
                case RELOCATE -> {
                    state = CState.WAITING_TRANSITION;
                    return pos.getX() + rayLength;
                }
                case SPAWN -> state = CState.TOUCHED_SPAWNER;
                case DISPLACEABLE -> {
                    state = CState.MOVING_STONE;
                    return pos.getX() + rayLength;
                }
                //case SWITCH -> state = CState.TOUCHED_SWITCH;
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
                case KILL -> {
                    state = CState.IS_DYING;
                    return pos.getY() + rayLength;
                }
                case NUDGE, DISPLACEABLE -> {
                    verticalDir = Direction.BACK;
                    verticalSpeed = 0;
                    return pos.getY() + rayLength;
                }
                case RELOCATE -> {
                    state = CState.WAITING_TRANSITION;
                    return pos.getY() + rayLength;
                }
                case SWITCH -> state = CState.TOUCHED_SWITCH;
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
                case KILL -> state = CState.IS_DYING;
                case NUDGE, DISPLACEABLE -> verticalDir = Direction.NO;
                case RELOCATE -> state = CState.WAITING_TRANSITION;
                //case SWITCH -> {
                //    state = CState.TOUCHED_SWITCH;
                //    return pos.getY() + verticalSpeed;
                //}
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
                case KILL:
                    state = CState.IS_DYING;
                    return 0;
                case NUDGE, DISPLACEABLE:
                    var rayUp = getClosestCeil(solids);
                    var rayUpLength = (rayUp == null) ? Double.MAX_VALUE : rayUp.getLength(false, true);
                    var actionUp = (rayUp == null) ? CAction.NUDGE : rayUp.getSolid().getAction();
                    verticalSpeed = instantVertAcc;
                    if (Math.abs(rayUpLength) < Math.abs(verticalSpeed)) {
                        switch (actionUp) {
                            case KILL:
                                state = CState.IS_DYING;
                                return 0;
                            case NUDGE, DISPLACEABLE:
                                verticalDir = Direction.BACK;
                                verticalSpeed = 0;
                                return pos.getY() + rayUpLength;
                            case RELOCATE:
                                state = CState.WAITING_TRANSITION;
                                return 0;
                            //case SWITCH:
                            //    state = CState.TOUCHED_SWITCH;
                            //    return pos.getY() + verticalSpeed;
                            default:
                                return 12;
                        }
                    } else verticalDir = Direction.FORWARD;

                    return pos.getY() + verticalSpeed;
                case RELOCATE:
                    state = CState.WAITING_TRANSITION;
                    return 0;
                //case SWITCH:
                //    state = CState.TOUCHED_SWITCH;
                //    return pos.getY() + verticalSpeed;
                default:
                    return 12;
            }
        } else {
            switch (action) {
                case KILL:
                    state = CState.IS_DYING;
                    return 0;
                case NUDGE, DISPLACEABLE:
                    verticalSpeed = 0;
                    return pos.getY();
                case RELOCATE:
                    state = CState.WAITING_TRANSITION;
                    return 0;
                //case SWITCH:
                //    state = CState.TOUCHED_SWITCH;
                //    return pos.getY() + verticalSpeed;
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
            if (solid.getAction() != CAction.PASS && solid.getAction() != CAction.SPAWN && solid.getAction() != CAction.SWITCH && solid.getAction() != CAction.PRESS) {
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
            if (solid.getAction() != CAction.PASS && solid.getAction() != CAction.SWITCH && solid.getAction() != CAction.PRESS) {
                var ray = new Ray(pos, solid.getPos(), solid);
                var length = ray.getLength(true, false);
                if (Math.abs(length) < Double.MAX_VALUE && ray.endIsRighterThanStart() &&
                        (min == null || Math.abs(min.getLength(true, false)) > Math.abs(length) ||
                                Math.abs(min.getLength(true, false)) == Math.abs(length)
                                        && ray.getSolid().getType() == SType.CHARACTER)) {
                    min = ray;
                }
            }
        }
        return min;
    }

    public Ray getClosestLeft(ArrayList<Solid> solids) {
        Ray min = null;
        for (var solid : solids) {
            if (solid.getAction() != CAction.PASS && solid.getAction() != CAction.SWITCH && solid.getAction() != CAction.PRESS) {
                var ray = new Ray(pos, solid.getPos(), solid);
                var length = ray.getLength(true, false);
                if (Math.abs(length) < Double.MAX_VALUE && ray.endIsLefterThanStart() &&
                        (min == null || Math.abs(min.getLength(true, false)) > Math.abs(length) ||
                                Math.abs(min.getLength(true, false)) == Math.abs(length)
                                        && ray.getSolid().getType() == SType.CHARACTER)) {
                    min = ray;
                }
            }
        }
        return min;
    }

    public Ray getClosestPlatform(ArrayList<Solid> solids) {
        Ray min = null;
        for (var solid : solids) {
            if (solid.getAction() != CAction.PASS && solid.getAction() != CAction.SPAWN && solid.getAction() != CAction.SWITCH && solid.getAction() != CAction.PRESS) {
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

    public CAction getAction() {
        return action;
    }

    public SType getType() {
        return type;
    }
    //endregion Actions With Pos and Size

    @Override
    public String toString() {
        return pos.toString();
    }
}
