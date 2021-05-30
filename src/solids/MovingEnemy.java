package solids;

import game.*;

import java.awt.*;

public class MovingEnemy extends Enemy{

    public MovingEnemy(double x, double y, double height, int speed) {
        super(x, y, CAction.KILL, SType.ENEMY, height, speed);
    }
}

