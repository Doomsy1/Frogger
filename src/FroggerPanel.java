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
	private static final int FPS = 60;
	private int screen = GAME; // Change to intro
	private int score;
	private LifeCounter lifeCounter = new LifeCounter(400, 0, 3);
	private TimerBar timerBar = new TimerBar(0, 0, 100, 20, 10, FPS);

	private final boolean[] keys;
	private final boolean[] keyPressed;
	Timer timer;

	private final Frog frog;

	private final ArrayList<Car> cars;
	private final ArrayList<Log> logs;
	private final ArrayList<LilyPad> lilyPads;

	private final ArrayList<Goal> goals;

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
		createCars(700, 50, 4, true, Car.RED, 3);
		createCars(650, 50, 3, false, Car.RED, 3);
		createCars(600, 50, 6, true, Car.RED, 3);
		createCars(550, 50, 4, false, Car.RED, 3);
		createCars(500, 75, 5, true, Car.RED, 3);

		// Logs and lily pads
		logs = new ArrayList<>();
		lilyPads = new ArrayList<>();
		createLilyPads(350, 150, 5, true, 4);
		createLogs(300, 150, 3, false, 3);
		createLogs(250, 200, 5, false, 2);
		createLilyPads(200, 100, 5, true, 4);
		createLogs(150, 175, 2, false, 3);


		// Goals
		goals = new ArrayList<>();
		createGoals(75, 100, 5, 150);

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
		timer = new Timer(1000 / FPS, this);
		timer.start();
	}

	private void createCars(int y, int width, int speed, boolean left, int color, int count) {
		int gap = (800 / (count));
		for (int i = 0; i < count; i++) {
			cars.add(new Car(gap * i, y, width, speed, left, color));
		}
	}

	private void createLogs(int y, int width, int speed, boolean left, int count) {
		int gap = (800 / (count));
		for (int i = 0; i < count; i++) {
			logs.add(new Log(gap * i, y, width, speed, left));
		}
	}

	private void createLilyPads(int y, int width, int speed, boolean left, int count) {
		int gap = (800 / (count));
		for (int i = 0; i < count; i++) {
			boolean breathing = (i == count - 1); // Only the last lily pad breathes
			lilyPads.add(new LilyPad(gap * i, y, width, speed, left, breathing));
		}
	}

	private void createGoals(int x, int y, int count, int gap) {
		for (int i = 0; i < count; i++) {
			goals.add(new Goal(gap * i + x, y));
		}
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

		// Check if the frog is riding a lily pad
		for (LilyPad lp : lilyPads) {
			if (frog.isRiding(lp)) {
				return false;
			}
		}

		// Check if the frog is colliding with a car
		for (Car c : cars) {
			if (frog.isColliding(c)) {
				return true;
			}
		}

		// Check if the frog is over the time limit
		if (timerBar.isOverTimeLimit()) {
			return true;
		}

		// Check if the frog is colliding with water
		return frog.isColliding(water) && !frog.inAir();
	}

	public void step() {
		if (screen == INTRO) {

		} else if (screen == GAME) {
			// Update time spent
			timerBar.update();

			// Move frog
			frog.move(keyPressed);

			// Check for goal
			for (Goal goal : goals) {
				if (frog.isColliding(goal)) {
					if (goal.fill()) {
						frog.reset();
						addScore(100);
						timerBar.reset();
					}
				}
			}

			// Check for death
			if (checkDeath()) {
				screen = GAME; // TEST
				frog.reset();
				timerBar.reset();
				lifeCounter.loseLife();
			}

			// Move cars
			for (Car c : cars) {
				c.move();
			}

			// Move logs
			for (Log l : logs) {
				l.move();
				if (frog.isRiding(l)) {
					frog.slide(l);
				}
			}

			// Move lily pads
			for (LilyPad lp : lilyPads) {
				lp.move();
				if (frog.isRiding(lp)) {
					frog.slide(lp);
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

		} else if (screen == GAME) {

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

			// Draw lily pads
			for (LilyPad lp : lilyPads) {
				lp.draw(g);
			}

			// Draw goals
			for (Goal goal : goals) {
				goal.draw(g);
			}

			// Draw frog
			frog.draw(g);

			// Draw timer bar
			timerBar.draw(g);

			// Draw life counter
			lifeCounter.draw(g);

			// Draw score
			g.setFont(fontComic);
			g.drawString("" + score, 50, 700);
		}
	}
}
