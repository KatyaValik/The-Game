package levels;

import java.awt.*;
import java.util.ArrayList;

import game.*;
import solids.*;

public class Level1 implements Level {
    private final ArrayList<Solid> solids = new ArrayList<>() {
        {
            add(new Construct(300, 279, 300, 9));
            add(new Construct(0, 367, 30, 9));
            add(new Construct(100, 344, 50, 9));
            add(new Construct(250, 344, 100, 9));
            add(new Construct(250, 310, 100, 9));
            add(new Construct(0, 0, 1, 1000));
            add(new Construct(31, 50, 989, 1));
            add(new Construct(31, 51, 1, 250));
            add(new Construct(1000, 0, 1, 1000, CAction.PUSH));
            add(new Construct(0, 500, 1000, 1, CAction.KILL));

            add(new Teleport(15, 332, 20));
            add(new Teleport(125, 310, 20));

            add(new Gate(599, 204, 1, 5, "level2", new Point(15, 352)));
            add(new Gate(599, 234, 1, 5, "level2", new Point(15, 352)));
            add(new Gate(599, 224, 1, 5, "level2", new Point(15, 352)));
            add(new Gate(599, 214, 1, 5, "level2", new Point(15, 352)));
            add(new Gate(599, 254, 1, 5, "level2", new Point(15, 352)));
            add(new Gate(599, 264, 1, 5, "level2", new Point(15, 352)));
            add(new Gate(599, 274, 1, 5, "level2", new Point(15, 352)));
            add(new Gate(599, 244, 1, 5, "level2", new Point(15, 352)));
        }
    };

    public ArrayList<Solid> getSolids() {
        return solids;
    }

    public double getG() {
        return 9.8;
    }

    public void update() {
    }
}
