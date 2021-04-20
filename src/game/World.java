package game;

import levels.*;
import solids.Gate;
import solids.Solid;

import java.awt.*;
import java.util.ArrayList;

public class World {
    private Character character;
    private double globalVertAcc = 0.1;
    private Level currentLevel;
    private Level spawnLevel;
    private Point spawnPos;
    //здесь должны быть уровни и тп и тд, но пока их нет(

    public World() {
        character = new Character(20, 20);
        LevelLoader.saveAll();
        currentLevel = LevelLoader.load("level1");
        spawnLevel = currentLevel;
        spawnPos = new Point(15, 352);
    }

    public void moveCharacter(Direction x, Direction y) {
        character.move(x, y, globalVertAcc, currentLevel.getSolids());
        switch (character.getState()) {
            case IS_DYING -> {
                currentLevel = spawnLevel;
                character.fastTeleport(spawnPos.getX(), spawnPos.getY());
            }
            case TOUCHED_SPAWNER -> {
                spawnLevel = currentLevel;
                spawnPos = character.getIntPos();
                character.normalizeState();
            }
            case WAITING_TRANSITION -> {
                var c = character.getTriggeredSolid();
                var gate = (Gate) (character.getTriggeredSolid());
                currentLevel = LevelLoader.load(gate.getAdjacentLevel());
                character.setPos(gate.getSpawnPos());
                character.normalizeState();
            }
        }
    }

    public Point getCharacterPos() {
        return character.getIntPos();
    }

    public ArrayList<Solid> getSolids() {
        return currentLevel.getSolids();
    }

    public Dimension getCharacterSize() {
        return character.getIntSize();
    }
}
