/*
 * Frogger.java
 * Ario Barin Ostovary
 * This runs the game
 */

import javax.swing.*;

public class Frogger extends JFrame {
    FroggerPanel game = new FroggerPanel();

    public Frogger() {
        super("Frogger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game);
        pack();
        setVisible(true);
    }

    public static void main(String[] arguments) {
        new Frogger();
    }
}
