package levels;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import solids.*;

public class Level2 implements Level {
    private final String name = "level2";

    private final ArrayList<Solid> solids = new ArrayList<>() {
        {
            add(new Construct(575, 300, 18, 25));
            add(new Construct(593, 300, 15, 10));
            add(new Construct(608, 300, 18, 25));

            add(new PressurePlate(446, 408, 30, 3));
            add(new Switch(500, 400, false));
            add(new PressurePlate(526, 408, 30, 3));

            add(new Construct(573, 406, 2, 4));

            add(new Construct(0, 0, 1100, 300));
            add(new Construct(0, 0, 200, 1000));
            add(new Construct(0, 450, 1100, 500));
            add(new Construct(1100, 0, 500, 1500));

            add(new Construct(300, 430, 275, 20));
            add(new Construct(350, 410, 225, 20));
            add(new Construct(1075, 410, 25, 40));

            add(new Gate(200, 300, 10, 150, "level1", new Point(1079, 439)));

            add(new Killer(575, 448, 500, 2));
            add(new Teleport(375, 300, 110));
        }
    };

    private final ArrayList<Stone> stones = new ArrayList<>();
    private final ArrayList<Switch> switches = new ArrayList<>();
    private final ArrayList<PressurePlate> plates = new ArrayList<>();
    private final HashMap<SType, Double> globalVertAcc = new HashMap<>() {
        {
            put(SType.CHARACTER, 0.1);
            put(SType.STONE, 0.1);
        }
    };

    public Level2() {
        for (Solid solid : solids) {
            if (solid instanceof Stone) {
                stones.add((Stone) solid);
            } else if (solid instanceof Switch) {
                switches.add((Switch) solid);
            } else if (solid instanceof PressurePlate) {
                plates.add((PressurePlate) solid);
            }
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

    public String getName() {
        return name;
    }

    private int timer = 0;

    public void update() {
        if (plates.get(0).getState()) {
            if (solids.get(0).getIntPos().x > 575) {
                for (int i = 0; i < 3; i++) {
                    moveConstruct((Construct) solids.get(i), true);
                }
            }
        }
        if (plates.get(1).getState()) {
            if (solids.get(2).getIntPos().x < 1057) {
                for (int i = 0; i < 3; i++) {
                    moveConstruct((Construct) solids.get(i), false);
                }
            }
        }
        if (switches.get(0).getState()) {
            if (timer > 100) {
                timer = 0;
                var stone = new Stone(solids.get(1).getPos().getX() + 2, solids.get(1).getPos().getY() + solids.get(1).getPos().getHeight() + 2);
                solids.add(stone);
                stones.add(stone);
            } else {
                timer++;
            }
        }
    }

    private void moveConstruct(Construct construct, boolean state) {
        var shift = 2;
        if (state) {
            construct.setPos(construct.getPos().getX() - shift, construct.getPos().getY());
        } else {
            construct.setPos(construct.getPos().getX() + shift, construct.getPos().getY());
        }
    }
}
