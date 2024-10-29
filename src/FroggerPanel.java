/* PongPanel.java
 *
 **/

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

class FroggerPanel extends JPanel implements KeyListener, ActionListener, MouseListener {
	public static final int INTRO = 0, GAME = 1, END = 2;
	public static final int WIDTH = 800, HEIGHT = 800;
	private int screen = GAME; // Change to intro
	private int score;

	private final boolean[] keys;
	private final boolean[] keyPressed;
	Timer timer;

	private final Frog frog;

	private final ArrayList<Car> cars;
	private final ArrayList<Log> logs;

	private final Terrain grass;
	private final Terrain water;

	private final Font fontComic; // Change font

	public FroggerPanel() {
		keys = new boolean[KeyEvent.KEY_LAST + 1];
		keyPressed = new boolean[KeyEvent.KEY_LAST + 1];

		// Frog
		frog = new Frog(this, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN);

		// Cars
		cars = new ArrayList<>();
		cars.add(new Car(Car.RED, 0, 400, 50, 10, true));
		cars.add(new Car(Car.RED, 0, 500, 50, 10, false));
		cars.add(new Car(Car.RED, 0, 600, 50, 10, true));

		// Logs
		logs = new ArrayList<>();
		logs.add(new Log(3, 0, 100, 10, true));
		logs.add(new Log(3, 0, 200, 10, false));
		logs.add(new Log(3, 0, 350, 10, true));

		// Terrain
		water = new Terrain(0, 0, WIDTH, HEIGHT / 2, Terrain.WATER);
		grass = new Terrain(0, HEIGHT / 2, WIDTH, HEIGHT / 2, Terrain.GRASS);


		score = 0;
		fontComic = new Font("Comic Sans MS", Font.PLAIN, 32);

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		addMouseListener(this);
		timer = new Timer(20, this);
		timer.start();
	}
	
	private boolean checkDeath() {
		// Check if frog is off screen
		if (frog.offScreen()) {
			return true;
		}

		// Check if the frog is riding a log
		for (Log l : logs) {
			if (frog.isRiding(l)) {
				return false;
			}
		}

		// Check if the frog is colliding with a car
		for (Car c : cars) {
			if (frog.isColliding(c)) {
				return true;
			}
		}

		// Check if the frog is colliding with water
		return frog.isColliding(water) && !frog.inAir();
	}

	public void step() {
		if (screen == INTRO) {

		} else if (screen == GAME) {
			// Move frog
			frog.move(keyPressed);

			// Check for death
			if (checkDeath()) {
				screen = GAME; // TEST
				frog.reset();
			}

			// Move cars and logs
			for (Car c : cars) {
				c.move();
			}
			for (Log l : logs) {
				l.move();
				if (frog.isRiding(l)) {
					frog.slide(l);
				}
			}
		}
		
		for (int i = 0; i < keyPressed.length; i++) {
			keyPressed[i] = false;
		}
	}

	public void addScore(int score) {
		this.score += score;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		step();
		repaint();

	}

	@Override
	public void keyReleased(KeyEvent ke) {
		int key = ke.getKeyCode();
		keys[key] = false;
		keyPressed[key] = false;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		int key = ke.getKeyCode();
		if (!keys[key]) {
			keys[key] = true;
			keyPressed[key] = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (screen == INTRO) {
			screen = GAME;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void paint(Graphics g) {
		if (screen == INTRO) {

		} 
		else if (screen == GAME) {

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());

			// Draw terrain
			grass.draw(g);
			water.draw(g);

			// Draw cars
			for (Car c : cars) {
				c.draw(g);
			}

			// Draw logs
			for (Log l : logs) {
				l.draw(g);
			}

			// Draw frog
			frog.draw(g);

			// Draw score
			g.setFont(fontComic);
			g.drawString("" + score, 50, 700);
		}
	}
}
