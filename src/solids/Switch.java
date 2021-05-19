package solids;

import game.CAction;

public class Switch extends Construct {
    private Boolean state;

    public Switch(double x, double y, boolean state) {
        super(x, y, 5, 5, CAction.SWITCH);
        this.state = state;
    }

    public void toggle(){
        state = !state;
    }

    public boolean getState(){
        return state;
    }
}
