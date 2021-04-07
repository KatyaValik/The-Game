public class Switch extends Entity {
    private boolean state;
    public Switch(double x, double y, double h, double w, boolean state){
        this.setPosAndSize(x, y, h, w);
        this.state = false;
    }
    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
    @Override
    public boolean isActivated() {
        return true;
    }

    @Override
    public boolean isMoveable() {
        return false;
    }

    @Override
    public boolean isBarrier() {
        return false;
    }

    @Override
    public void Activate() {
        this.state = !state;
    }
}
