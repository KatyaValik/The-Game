package solids;

import game.CAction;

public class Switch extends Construct {
    public Boolean state;

    public Switch(double x, double y, boolean state) {
        super(x, y, 10, 10, CAction.SWITCH);
        this.state = state;
    }

    public void toggle(){
        state = !state;
    }
}
