package solids;

import game.*;

import java.awt.*;
import java.io.Serializable;

public interface Solid extends Serializable {
    Point getIntPos();

    Dimension getIntSize();

    Position getPos();

    CAction getAction();

    SType getType();
}
