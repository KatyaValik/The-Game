package levels;

import game.CAction;
import solids.Gate;
import solids.Solid;
import solids.Construct;
import solids.Stone;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Level2 implements Level {
    private final String name = "level2";

    private final ArrayList<Solid> solids = new ArrayList<>() {
        {
            add(new Construct(300, 279, 300, 9));
            add(new Construct(0, 367, 30, 9));
            add(new Construct(100, 344, 50, 9));
            add(new Construct(250, 344, 100, 9));
            add(new Construct(250, 310, 100, 9));
            add(new Construct(0, 0, 1, 1000));
            add(new Construct(1000, 0, 1, 1000, CAction.PUSH));
            add(new Construct(0, 500, 1000, 1, CAction.KILL));

            add(new Gate(1, 1, 1, 1000, "level1", new Point(588, 254)));
        }
    };

    private final ArrayList<Stone> stones = new ArrayList<>();
    private HashMap<Stone, Double> stonesVertAcc;

    public double getStoneVertAcc(Stone stone) {
        return stonesVertAcc.get(stone);
    }

    public ArrayList<Stone> getStones() {
        return stones;
    }

    public ArrayList<Solid> getSolids() {
        return solids;
    }

    public String getName() {
        return name;
    }

    public void update() {

    }
}
