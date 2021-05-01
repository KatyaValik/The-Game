package solids;

import game.CAction;

public class Character extends Movable {
    public Character(double x, double y) {
        super(x, y, CAction.NUDGE, SType.CHARACTER);
    }
}