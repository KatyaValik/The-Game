package game;

import solids.MovableSolid;
import solids.SType;

public class Character extends MovableSolid {
    public Character(double x, double y) {
        super(x, y, CAction.NUDGE, SType.CHARACTER);
    }
}