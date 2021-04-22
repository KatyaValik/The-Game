package game;

import solids.Solid;

public class Ray {
    private Solid solid;
    private Position start;
    private Position end;

    public Ray(Position start, Position end, Solid solid) {
        this.start = start;
        this.end = end;
        this.solid = solid;
    }

    public boolean endIsRighterThanStart() {
        var right = start.getRight();
        var left = end.getLeft();
        return right.getX1() <= left.getX1();
    }

    public boolean endIsLefterThanStart() {
        var left = start.getLeft();
        var right = end.getRight();
        return left.getX1() >= right.getX1();
    }

    public boolean endIsUponStart() {
        var top = start.getTop();
        var bottom = end.getBottom();
        return top.getY1() >= bottom.getY1();
    }

    public boolean endIsUnderStart() {
        var top = end.getTop();
        var bottom = start.getBottom();
        return top.getY1() >= bottom.getY1();
    }

    //region getLength and hasInter
    public double getLength(boolean x, boolean y) {
        var inX = hasInter(true, false, true);
        var inY = hasInter(false, true, true);
        if (inX && inY)
            return 0;
        if (inX && y)
            return getProjection(start.getTop().getY1(), start.getBottom().getY1(),
                    end.getTop().getY1(), end.getBottom().getY1());
        if (inY && x)
            return getProjection(start.getLeft().getX1(), start.getRight().getX1(),
                    end.getLeft().getX1(), end.getRight().getX1());
        if (x && y) return start.getPos().distance(end.getPos());
        else return Double.MAX_VALUE;
    }

    private double getProjection(double f_min, double f_max, double s_min, double s_max) {
        var first = s_min - f_max;
        var second = s_max - f_min;
        if (Math.min(Math.abs(first), Math.abs(second)) == Math.abs(first))
            return first;
        else return second;
    }

    public boolean hasInter(boolean x, boolean y, boolean withBorders) {
        var inX = true;
        var inY = true;
        if (x)
            inX = hasInter(start.getLeft().getX1(), start.getRight().getX1(),
                    end.getLeft().getX1(), end.getRight().getX1(), withBorders);
        if (y)
            inY = hasInter(start.getTop().getY1(), start.getBottom().getY1(),
                    end.getTop().getY1(), end.getBottom().getY1(), withBorders);
        return inX && inY;
    }

    private boolean hasInter(double f_min, double f_max, double s_min, double s_max, boolean withBorders) {
        var in = f_min < s_min && s_min < f_max || s_min < f_min && f_min < s_max;
        in = in || withBorders && (f_min == s_max || f_max == s_max || f_max == s_min || f_min == s_min);
        return in;
    }
    //endregion getLength and hasInter

    public Solid getSolid() {
        return solid;
    }

    @Override
    public String toString() {
        return "s: " + start.getPos() + " e: " + end.getPos();
    }
}
