import java.awt.*;
import java.util.ArrayList;
import java.util.function.Function;

public abstract class Level {
    protected ArrayList<Entity> entities;
    protected double g;
    protected abstract void update();
}
