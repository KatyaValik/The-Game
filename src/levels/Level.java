package levels;

import solids.*;

import java.io.Serializable;
import java.util.ArrayList;

public interface Level extends Serializable {
    ArrayList<Solid> getSolids();

    ArrayList<Stone> getStones();

    ArrayList<PressurePlate> getPlates();

    void update();

    String getName();

    double getGlobalVertAcc(SType type);
}
