import java.awt.*;

public class World {
    private Character character;
    private double globalVertAcc = 0.1;
    private double globalPlat = 300;
    //здесь должны быть уровни и тп и тд, но пока их нет(
    private Level currentLevel;


    public World() {
        character = new Character(20, 20);
    }

    public void moveCharacter(Direction x, Direction y) {
        character.moveHor(x);
        if (character.getPos().y == globalPlat && y == Direction.FORWARD)
            character.moveVert(y, 0, globalPlat);
        else if (character.getPos().y < globalPlat)
            character.moveVert(Direction.BACK, globalVertAcc, globalPlat);
        else character.moveVert(Direction.NO, globalVertAcc, globalPlat);
    }

    public Point getCharacterPos() {
        return character.getPos();
    }
}

