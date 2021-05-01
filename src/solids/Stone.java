package solids;

import game.*;

public class Stone extends Movable {

    public Stone(double x, double y) {
        super(x, y, CAction.DISPLACEABLE, SType.STONE);
        setSize(10.1, 10.1);
    }
}