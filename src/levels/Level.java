package levels;

import solids.*;

import java.io.Serializable;
import java.util.ArrayList;

public interface Level extends Serializable {
    ArrayList<Solid> getSolids();

    double getG();

    void update();
}
