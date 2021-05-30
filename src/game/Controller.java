package game;

import solids.PressurePlate;
import solids.Solid;
import solids.Switch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class    Controller extends JPanel{
    private Set<Integer> keys = new HashSet<>();
    private World world = new World();
    private Timer timer = new Timer(10, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Direction horDir = chooseHorDir();
            Direction vertDir = chooseVertDir();
            var isActionButtonPressed = keys.contains(KeyEvent.VK_E);
            world.moveCharacter(horDir, vertDir, isActionButtonPressed);
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
        setFocusable(true);
        setDoubleBuffered(true);
        timer.start();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                    System.exit(0);
                }
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
        var solids = world.getSolids();
        for (Solid solid : solids) {
            Point solidPos = solid.getIntPos();
            Dimension solidSize = solid.getIntSize();
            switch (solid.getAction()) {
                case KILL -> g2.setColor(Color.red);
                case RELOCATE -> g2.setColor(Color.yellow);
                case SPAWN -> g2.setColor(Color.green);
                case DISPLACEABLE -> g2.setColor(Color.pink);
                case SWITCH -> {
                    var sw = (Switch) solid;
                    if (sw.getState())
                        g2.setColor(Color.darkGray);
                    else
                        g2.setColor(Color.gray);
                }
                case PRESS -> {
                    var sw = (PressurePlate) solid;
                    if (sw.getState())
                        g2.setColor(Color.darkGray);
                    else
                        g2.setColor(Color.gray);
                }
                case MOVING -> g2.setColor(Color.white);
                default -> g2.setColor(Color.lightGray);
            }
            switch (solid.getType()) {
                case CONSTRUCT -> g2.fillRect(solidPos.x, solidPos.y, solidSize.width, solidSize.height);
                case STONE -> g2.drawOval(solidPos.x, solidPos.y, solidSize.width, solidSize.height);
                case ENEMY -> g2.drawOval(solidPos.x, solidPos.y, solidSize.width, solidSize.height);
            }
        }
        g2.setColor(Color.cyan);
        var c = world.getCharacterPos();
        var cSize = world.getCharacterSize();
        g2.drawOval(c.x, c.y, cSize.width, cSize.height);
    }
}
