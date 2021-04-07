public class Platform extends Entity {
    public Platform(double x, double y, double h, double w){
        this.setPosAndSize(x, y, h, w);
    }
    @Override
    public boolean isActivated() {
        return false;
    }

    @Override
    public boolean isMoveable() {
        return false;
    }

    @Override
    public boolean isBarrier() {
        return true;
    }

    @Override
    public void Activate() {
        throw new RuntimeException("platform is not active");
    }
}
