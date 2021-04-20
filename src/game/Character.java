package game;

import solids.MoveableSolid;
import solids.SType;

public class Character extends MoveableSolid {

    public Character(double x, double y) {
        super(x, y, CAction.NUDGE, SType.CHARACTER);
    }
}