package solids;

import game.*;

public class Stone extends MovableSolid {

    public Stone(double x, double y) {
        super(x, y, CAction.SLAVE, SType.STONE);
    }
}