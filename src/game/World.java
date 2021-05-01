package game;

import levels.*;
import solids.Character;
import solids.Gate;
import solids.SType;
import solids.Solid;
import solids.Stone;

import java.awt.*;
import java.util.ArrayList;

public class World {
    private solids.Character character;
    private Level currentLevel;
    private String spawnLevelName;
    private Point spawnPos;
    //здесь должны быть уровни и тп и тд, но пока их нет(

    public World() {
        character = new Character(20, 20);
        LevelLoader.saveAll();
        currentLevel = LevelLoader.load("level1");
        spawnLevelName = currentLevel.getName();
        spawnPos = new Point(450, 265);
    }

    public void moveCharacter(Direction x, Direction y, Boolean isActionButtonPressed) {
        for (Stone stone : currentLevel.getStones()) {
            var solids = new ArrayList<>(currentLevel.getSolids());
            solids.add(character);
            solids.remove(stone);
            stone.move(Direction.NO, Direction.NO, currentLevel.getGlobalVertAcc(stone.getType()), solids);
        }

        character.move(x, y, currentLevel.getGlobalVertAcc(SType.CHARACTER), currentLevel.getSolids());

        switch (character.getState()) {
            case IS_DYING -> {
                LevelLoader.save(currentLevel, currentLevel.getName());
                currentLevel = LevelLoader.load(spawnLevelName);
                character.fastTeleport(spawnPos.getX(), spawnPos.getY());
            }
            case TOUCHED_SPAWNER -> {
                spawnLevelName = currentLevel.getName();
                spawnPos = character.getIntPos();
                character.normalizeState();
            }
            case WAITING_TRANSITION -> {
                var gate = (Gate) (character.getTriggeredSolid());
                LevelLoader.save(currentLevel, currentLevel.getName());
                currentLevel = LevelLoader.load(gate.getAdjacentLevel());
                character.setPos(gate.getSpawnPos());
                character.normalizeState();
            }
            case MOVING_STONE -> {
                var stone = (Stone) (character.getTriggeredSolid());
                stone.move(x, Direction.NO, currentLevel.getGlobalVertAcc(SType.STONE), currentLevel.getSolids());
                stone.normalizeState();
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
