package solids;

import game.CAction;

public class Killer extends Construct {
    public Killer(double x, double y, double width, double height) {
        super(x, y, width, height, CAction.KILL);
    }
}
