package solids;

import game.*;

public class Stone extends MoveableSolid {

    public Stone(double x, double y) {
        super(x, y, CAction.SLAVE, SType.STONE);
    }
}