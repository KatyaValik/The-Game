package levels;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LevelLoader {
    static public void save(Level level, String levelName) {
        try {
            FileOutputStream out = new FileOutputStream("src/levels/" + levelName);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(level);
            oos.close();
        } catch (Exception e) {
            System.out.println("Problem serializing: " + e);
        }
    }

    static public void saveAll() {
        save(new Level1(), "level1");
        save(new Level2(), "level2");
    }

    static public Level load(String levelName) {
        try {
            FileInputStream in = new FileInputStream("src/levels/" + levelName);
            ObjectInputStream ois = new ObjectInputStream(in);
            return (Level) (ois.readObject());
        } catch (Exception e) {
            System.out.println("Problem serializing: " + e);
            return null;
        }
    }
}
