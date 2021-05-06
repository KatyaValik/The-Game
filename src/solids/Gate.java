package solids;

import game.CAction;

import java.awt.*;

public class Gate extends Construct {
    private final String adjacentLevel;
    private final Point spawnPos;

    public Gate(double x, double y, double width, double height, String adjacentLevel, Point spawnPos) {
        super(x, y, width, height, CAction.RELOCATE);
        this.adjacentLevel = adjacentLevel;
        this.spawnPos = spawnPos;
    }

    public String getAdjacentLevel() {
        return adjacentLevel;
    }

    public Point getSpawnPos() {
        return spawnPos;
    }
}
