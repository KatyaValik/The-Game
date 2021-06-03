package levels;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import solids.*;

public class Level1 implements Level {
    private final String name = "level1";

    private final ArrayList<Solid> solids = new ArrayList<>() {
        {
            add(new Construct(800, 300, 10, 150));
            add(new Construct(820, 300, 10, 150));
            add(new Construct(840, 300, 10, 150));
            add(new Construct(860, 300, 10, 150));
            add(new Construct(880, 300, 10, 150));
            add(new Construct(900, 300, 10, 150));
            add(new Construct(920, 300, 10, 150));
            add(new Construct(940, 300, 10, 150));
            add(new Construct(960, 300, 10, 150));
            add(new Construct(980, 300, 10, 150));
            add(new Construct(1000, 300, 10, 150));

            add(new PressurePlate(450, 473, 30, 3));
            add(new PressurePlate(500, 473, 30, 3));
            add(new PressurePlate(550, 473, 30, 3));
            add(new PressurePlate(600, 473, 30, 3));
            add(new PressurePlate(650, 473, 30, 3));
            add(new PressurePlate(700, 473, 30, 3));

            add(new Construct(0, 0, 1100, 300));
            add(new Construct(0, 0, 200, 1000));
            add(new Construct(0, 475, 1100, 500));
            add(new Construct(1100, 0, 500, 1500));

            add(new Construct(0, 450, 400, 50));
            add(new Construct(750, 450, 400, 50));

            add(new Stone(570, 463));

            add(new Switch(225, 440, false));
            add(new Switch(250, 440, false));
            add(new Switch(275, 440, false));
            add(new Switch(300, 440, false));
            add(new Switch(325, 440, false));
            add(new Switch(350, 440, false));
            add(new Switch(375, 440, false));

            add(new Gate(1090, 300, 10, 150, "level2", new Point(210, 439)));

            add(new MovingEnemy(1050, 440, 100.0, 1));
            add(new MovingEnemy(485, 465, 110.0, 1));
            add(new MovingEnemy(535, 465, 120.0, 1));
            add(new MovingEnemy(585, 465, 130.0, 1));
            add(new MovingEnemy(635, 465, 140.0, 1));
            add(new MovingEnemy(685, 465, 150.0, 1));
        }
    };

    private final ArrayList<Stone> stones = new ArrayList<>();
    private final ArrayList<Switch> switches = new ArrayList<>();
    private final ArrayList<PressurePlate> plates = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final HashMap<SType, Double> globalVertAcc = new HashMap<>() {
        {
            put(SType.CHARACTER, 0.1);
            put(SType.STONE, 0.1);
        }
    };

    public Level1() {
        for (Solid solid : solids) {
            if (solid instanceof Stone) {
                stones.add((Stone) solid);
            } else if (solid instanceof Switch) {
                switches.add((Switch) solid);
            } else if (solid instanceof PressurePlate) {
                plates.add((PressurePlate) solid);
            } else if (solid instanceof Enemy)
                enemies.add((Enemy) solid);
        }
    }

    public double getGlobalVertAcc(SType type) {
        if (globalVertAcc.containsKey(type))
            return globalVertAcc.get(type);
        else return 0;
    }

    public ArrayList<Stone> getStones() {
        return stones;
    }

    public ArrayList<Solid> getSolids() {
        return solids;
    }

    public ArrayList<PressurePlate> getPlates() {
        return plates;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public String getName() {
        return name;
    }

    public void update() {
        moveConstruct((Construct) solids.get(0), switches.get(0).getState() & !plates.get(1).getState());
        moveConstruct((Construct) solids.get(1), switches.get(4).getState() & !plates.get(0).getState());
        moveConstruct((Construct) solids.get(2), !switches.get(6).getState() ^ !plates.get(5).getState());
        moveConstruct((Construct) solids.get(3), !switches.get(1).getState() ^ !plates.get(2).getState());
        moveConstruct((Construct) solids.get(4), !switches.get(0).getState() ^ !plates.get(4).getState());
        moveConstruct((Construct) solids.get(5), switches.get(4).getState() & !plates.get(5).getState());
        moveConstruct((Construct) solids.get(6), !switches.get(3).getState() & plates.get(3).getState());
        moveConstruct((Construct) solids.get(7), switches.get(5).getState() & plates.get(3).getState());
        moveConstruct((Construct) solids.get(8), switches.get(1).getState() & !plates.get(4).getState());
        moveConstruct((Construct) solids.get(9), switches.get(2).getState() & !plates.get(2).getState());
        moveConstruct((Construct) solids.get(10), switches.get(5).getState() & !plates.get(0).getState());
    }

    private void moveConstruct(Construct construct, boolean state) {
        if (state) {
            if (construct.getPos().getY() > 200) {
                construct.setPos(construct.getPos().getX(), construct.getPos().getY() - 1);
            }
        } else {
            if (construct.getPos().getY() < 300) {
                construct.setPos(construct.getPos().getX(), construct.getPos().getY() + 1);
            }
        }
    }
}
