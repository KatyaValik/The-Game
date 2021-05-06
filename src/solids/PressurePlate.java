package solids;

import game.CAction;
import game.CState;

import java.util.ArrayList;

public class PressurePlate extends Construct {
    private boolean state = false;

    public PressurePlate(double x, double y, double width, double height) {
        super(x, y, width, height, CAction.PRESS);
    }

    public void inspectCollision(ArrayList<Solid> solids) {
        state = false;
        for (Solid solid : solids) {
            if (solid.getType() != SType.CONSTRUCT &&
                    Math.max(pos.getX(), solid.getPos().getX()) <
                            Math.min(pos.getRight().getX2(), solid.getPos().getRight().getX2()) &&
                    Math.max(pos.getTop().getY1(), solid.getPos().getTop().getY1()) <
                            Math.min(pos.getBottom().getY1(), solid.getPos().getBottom().getY1())) {
                state = true;
                break;
            }
        }
    }

    public boolean getState() {
        return state;
    }

    public void press() {
        state = true;
    }

    public void stop() {
        state = false;
    }
}
