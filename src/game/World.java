package game;

import java.awt.*;
import java.util.ArrayList;

public class World {
    private Character character;
    private double globalVertAcc = 0.1;
    //здесь должны быть уровни и тп и тд, но пока их нет(

    private ArrayList<Solid> solids = new ArrayList<>() {
        {
            add(new Wall(300, 279, 300, 9));
            add(new Wall(0, 300, 1000, 1));
            add(new Wall (0, 0, 1, 1000));
            add(new Wall(500, 260, 1, 300, CAction.PUSH));
            add(new Wall(1000, 0, 1, 1000, CAction.PUSH));
        }
    };

    public World() {
        character = new Character(20, 20);
    }

    public void moveCharacter(Direction x, Direction y) {
        character.move(x, y, globalVertAcc, solids);
    }

    public Point getCharacterPos() {
        return character.getIntPos();
    }

    public ArrayList<Solid> getSolids() {
        return solids;
    }

    public Dimension getCharacterSize() {
        return character.getIntSize();
    }
}
