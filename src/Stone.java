public class Stone extends Entity {
    public Stone(double x, double y, double h, double w){
        this.setPosAndSize(x, y, h, w);
    }
    @Override
    public boolean isActivated() {
        return false;
    }

    @Override
    public boolean isMoveable() {
        return true;
    }

    @Override
    public boolean isBarrier() {
        return true;
    }

    @Override
    public void Activate() {
        throw new RuntimeException("stone is not active");
    }
}
