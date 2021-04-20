package game;

import solids.Solid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class Controller extends JPanel {
    private Set<Integer> keys = new HashSet<>();
    private World world = new World();
    private Timer timer = new Timer(10, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Direction horDir = chooseHorDir();
            Direction vertDir = chooseVertDir();
            world.moveCharacter(horDir, vertDir);
            invalidate();
            repaint();
        }
    });

    private Direction chooseHorDir() {
        if (keys.contains(KeyEvent.VK_RIGHT) == keys.contains(KeyEvent.VK_LEFT))
            return Direction.NO;
        else if (keys.contains(KeyEvent.VK_RIGHT))
            return Direction.FORWARD;
        else return Direction.BACK;
    }

    private Direction chooseVertDir() {
        if (keys.contains(KeyEvent.VK_UP))
            return Direction.FORWARD;
        else return Direction.CONTINUE;
    }

    public Controller() {
        super();
        setSize(640, 480);
        setPreferredSize(getSize());
        setFocusable(true);
        setDoubleBuffered(true);
        timer.start();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys.remove(e.getKeyCode());
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        var g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.cyan);
        var c = world.getCharacterPos();
        var cSize = world.getCharacterSize();
        g2.drawOval(c.x, c.y, cSize.width, cSize.height);
        var solids = world.getSolids();
        for (Solid solid : solids) {
            Point solidPos = solid.getIntPos();
            Dimension solidSize = solid.getIntSize();
            switch (solid.getAction()) {
                case KILL -> g2.setColor(Color.red);
                case RELOCATE -> g2.setColor(Color.yellow);
                case SPAWN -> g2.setColor(Color.green);
                case SLAVE -> g2.setColor(Color.pink);
                default -> g2.setColor(Color.lightGray);
            }
            switch (solid.getType()) {
                case CONSTRUCT -> g2.fillRect(solidPos.x, solidPos.y, solidSize.width, solidSize.height);
                case STONE -> g2.drawOval(solidPos.x, solidPos.y, solidSize.width, solidSize.height);
            }
        }
    }
}
