package game;

import levels.*;
import solids.*;
import solids.Character;

import java.awt.*;
import java.util.ArrayList;

public class World {
    private solids.Character character;
    private Level currentLevel;
    private String spawnLevelName;
    private Point spawnPos;
    private Boolean isActionButtonProcessed = false;

    public World() {
        character = new Character(20, 20);
        LevelLoader.saveAll();
        currentLevel = LevelLoader.load("level1");
        spawnLevelName = currentLevel.getName();
        spawnPos = new Point(300, 439);
    }

    public void moveCharacter(Direction x, Direction y, Boolean isActionButtonPressed) {
        moveEnemy();
        moveStones();
        inspectEnemy(x);
        if (!isActionButtonPressed && character.getState() == CState.MOVING_STONE && x != Direction.NO)
            character.normalizeState();

        if (isActionButtonPressed && character.getState() == CState.MOVING_STONE) {
            if (character.getTriggeredSolid() instanceof Stone)
                moveCharacterWithStone(x, y);
            else
                character.normalizeState();
        } else
            moveCharacterWithoutStone(x, y, isActionButtonPressed);
        inspectPlate();
        currentLevel.update();
    }

    public void moveStones() {
        for (Stone stone : currentLevel.getStones()) {
            var solids = new ArrayList<>(currentLevel.getSolids());
            solids.add(character);
            solids.remove(stone);
            stone.move(Direction.NO, Direction.NO, currentLevel.getGlobalVertAcc(stone.getType()), solids);
        }
    }

    public void inspectPlate() {
        for (PressurePlate plate : currentLevel.getPlates()) {
            var solids = new ArrayList<>(currentLevel.getSolids());
            solids.add(character);
            solids.remove(plate);
            plate.inspectCollision(solids);
        }
    }

    public void moveCharacterWithStone(Direction x, Direction y) {
        var stone = (Stone) (character.getTriggeredSolid());
        var solids = new ArrayList<>(currentLevel.getSolids());
        solids.add(character);
        solids.remove(stone);
        if (stone.isTouchingCharacter(x, solids)) {
            character.move(x, y, currentLevel.getGlobalVertAcc(SType.CHARACTER), currentLevel.getSolids());
            stone.move(x, Direction.NO, currentLevel.getGlobalVertAcc(SType.STONE), solids);
        } else if (stone.isTouchingCharacter(x == Direction.FORWARD ? Direction.BACK : Direction.FORWARD, solids)) {
            stone.move(x, Direction.NO, currentLevel.getGlobalVertAcc(SType.STONE), solids);
            character.move(x, y, currentLevel.getGlobalVertAcc(SType.CHARACTER), currentLevel.getSolids());
        } else if (x != Direction.NO || y == Direction.FORWARD) {
            character.move(x, y, currentLevel.getGlobalVertAcc(SType.CHARACTER), currentLevel.getSolids());
            character.normalizeState();
        }
        stone.normalizeState();
    }

    public void moveCharacterWithoutStone(Direction x, Direction y, Boolean isActionButtonPressed) {
        character.move(x, y, currentLevel.getGlobalVertAcc(SType.CHARACTER), currentLevel.getSolids());

        if (isActionButtonPressed)
            character.inspectCollision(currentLevel.getSolids());

        if (isActionButtonProcessed && !isActionButtonPressed)
            isActionButtonProcessed = false;

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
            case TOUCHED_SWITCH -> {
                if (isActionButtonPressed && !isActionButtonProcessed) {
                    var sw = (Switch) (character.getTriggeredSolid());
                    sw.toggle();
                    isActionButtonProcessed = true;
                }
                character.normalizeState();
            }
        }
    }

    public void inspectEnemy(Direction x) {
        var solids = new ArrayList<>(currentLevel.getSolids());
        for (Enemy enemy : currentLevel.getEnemies()) {
            if (enemy.inspectDeath(character)) {
                LevelLoader.save(currentLevel, currentLevel.getName());
                currentLevel = LevelLoader.load(spawnLevelName);
                character.fastTeleport(spawnPos.getX(), spawnPos.getY());
            }
        }

    }

    public void moveEnemy() {
        for (Enemy enemy : currentLevel.getEnemies()) {
            enemy.moveEnemy();
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
