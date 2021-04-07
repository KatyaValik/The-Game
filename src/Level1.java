import java.util.ArrayList;
import java.util.function.Function;

public class Level1 extends Level {
    private Platform platform = new Platform(0, 10, 2, 20);
    private Switch switch1 = new Switch(0, 10, 4,4,false);
    private Switch switch2 = new Switch(16, 10, 4,4,true);
    private Stone stone = new Stone(5, 5, 2, 2);

    private ArrayList<Entity> f(){
        var arr = new ArrayList();
        arr.add(this.platform);
        arr.add(this.switch1);
        arr.add(this.switch2);
        arr.add(this.stone);
        return arr;
    }
    public Level1(){
        this.entities = f();
        this.g = 9.8;
    }

    @Override
    protected void update() {
        switch2.setState(switch1.getState());
    }
}
