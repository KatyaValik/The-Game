import java.awt.geom.Line2D;

import java.awt.*;
import java.util.Vector;

public abstract class Entity {
    private double x;
    private double y;
    private double h;
    private double w;
    private String texture;

    public void setPosAndSize(double x, double y, double h, double w) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }
    public Vector<Integer> getPosAndSize() {
        var v = new Vector<Integer>(4);
        v.add(Math.toIntExact(Math.round(Math.floor(this.x))));
        v.add(Math.toIntExact(Math.round(Math.floor(this.y))));
        v.add(Math.toIntExact(Math.round(Math.floor(this.h))));
        v.add(Math.toIntExact(Math.round(Math.floor(this.w))));
        return v;
    }
    public abstract boolean isActivated();
    public abstract boolean isMoveable();
    public abstract boolean isBarrier();
    public abstract void Activate();
}
