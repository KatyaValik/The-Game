package solids;

import game.*;


public class Teleport extends Construct {
    public Teleport(double x, double y, double height) {
        super(x, y, 1, height, CAction.SPAWN);
    }
}
