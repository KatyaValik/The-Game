package game;

import javax.swing.*;

public class Starter {
    public static void main(String[] args) {
        var frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Controller());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}