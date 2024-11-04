/*
 * FroggerPanel.java
 * Ario Barin Ostovary
 * This class controls the game's logic
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

class FroggerPanel extends JPanel implements KeyListener, ActionListener, MouseListener {
	public static final int INTRO = 0, GAME = 1, END = 2;
	public static final int WIDTH = 800, HEIGHT = 800;
	private static final int FPS = 60;
	private int screen = GAME; // Change to intro
	private int score = 0;
	private int level = 1;
	private LifeCounter lifeCounter = new LifeCounter(600, 750, 3);
	private TimerBar timerBar = new TimerBar(0, 780, 200, 20, 10, FPS);

	private final boolean[] keys;
	private final boolean[] keyPressed;
	Timer timer;

	private final Frog frog;

	private ArrayList<Car> cars;
	private ArrayList<Log> logs;
	private ArrayList<Turtle> Turtles;
	private ArrayList<Alligator> alligators;

	private ArrayList<Goal> goals;

	private ArrayList<Terrain> grasses;
	private Terrain water;
	private Terrain road;
	private ArrayList<Terrain> barriers;

	private final Font fontComic; // Change font

	public FroggerPanel() {
		keys = new boolean[KeyEvent.KEY_LAST + 1];
		keyPressed = new boolean[KeyEvent.KEY_LAST + 1];

		// Frog
		frog = new Frog(this, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN);

		initLevelOne(); // TEST

		fontComic = new Font("Comic Sans MS", Font.PLAIN, 32);

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		addMouseListener(this);
		timer = new Timer(1000 / FPS, this);
		timer.start();
	}

	private void initBase() {
		cars = new ArrayList<>();
		logs = new ArrayList<>();
		Turtles = new ArrayList<>();
		alligators = new ArrayList<>();
		goals = new ArrayList<>();
		barriers = new ArrayList<>();
		water = new Terrain(0, 0, WIDTH, HEIGHT / 2 - 50, Terrain.WATER);
		road = new Terrain(0, HEIGHT / 2 + 100, WIDTH, 250, Terrain.ROAD);
		grasses = new ArrayList<>();

		// Goals
		createGoals(75, 50, 5, 150);

		// Terrain
		barriers.add(new Terrain(0, 0, WIDTH, 50, Terrain.BARRIER));

		grasses.add(new Terrain(0, HEIGHT / 2 - 50, WIDTH, 150, Terrain.GRASS));
		grasses.add(new Terrain(0, HEIGHT - 50, WIDTH, 50, Terrain.GRASS));
	}

	private void initLevelOne() {
		initBase();
		
		// Floating objects
		createLogs(100, 175, 2, false, 3);
		createTurtles(150, 100, 5, true, 4);
		createLogs(200, 200, 5, false, 2);
		createLogs(250, 150, 3, false, 3);
		createTurtles(300, 150, 5, true, 4);

		// Cars
		createCars(500, 75, 5, true, Car.RED, 3);
		createCars(550, 50, 4, false, Car.RED, 3);
		createCars(600, 50, 6, true, Car.RED, 3);
		createCars(650, 50, 3, false, Car.RED, 3);
		createCars(700, 50, 4, true, Car.RED, 3);
	}

	private void initLevelTwo() {
		initBase();

		// Floating objects
		createAlligators(100, 250, 2, false, 2);
		createTurtles(150, 100, 5, true, 4);
		createLogs(200, 200, 7, false, 2);
		createLogs(250, 150, 3, false, 3);
		createTurtles(300, 150, 5, true, 4);

		// Cars
		createCars(500, 75, 6, true, Car.RED, 3);
		createCars(550, 50, 3, false, Car.RED, 2);
		createCars(600, 50, 5, true, Car.RED, 6);
		createCars(650, 50, 2, false, Car.RED, 4);
		createCars(700, 50, 4, true, Car.RED, 4);
	}

	private void initLevelThree() {
		initBase();

		// Floating objects
		createAlligators(100, 250, 2, false, 2);
		createTurtles(150, 100, 5, true, 4);
		createLogs(200, 200, 7, false, 2);
		createLogs(250, 150, 3, false, 3);
		createTurtles(300, 150, 5, true, 4);

		// Cars
		createCars(500, 75, 5, true, Car.RED, 3);
		createCars(550, 50, 9, false, Car.RED, 2);
		createCars(600, 50, 6, true, Car.RED, 6);
		createCars(650, 50, 3, false, Car.RED, 4);
		createCars(700, 50, 4, true, Car.RED, 4);
	}

	private void nextLevel() {
		level++;
		frog.reset();
		switch (level) {
			case 2 -> initLevelTwo();
		}
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

	private void createTurtles(int y, int width, int speed, boolean left, int count) {
		int gap = (800 / (count));
		for (int i = 0; i < count; i++) {
			boolean breathing = (i == count - 1); // Only the last turtle breathes
			Turtles.add(new Turtle(gap * i, y, width, speed, left, breathing));
		}
	}

	private void createAlligators(int y, int width, int speed, boolean left, int count) {
		int gap = (800 / (count));
		for (int i = 0; i < count; i++) {
			if (i == 0) { // First object is an alligator
				alligators.add(new Alligator(gap * i, y, width, speed, left));
			} else { // Rest are logs
				logs.add(new Log(gap * i, y, width, speed, left));
			}
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

		// Check if the frog is colliding with a barrier
		for (Terrain t : barriers) {
			if (frog.isColliding(t)) {
				return true;
			}
		}

		// Check if the frog is riding a log
		for (Log l : logs) {
			if (frog.isRiding(l)) {
				return false;
			}
		}

		// Check if the frog is riding a turtle
		for (Turtle t : Turtles) {
			if (frog.isRiding(t)) {
				return false;
			}
		}

		// Check if the frog is being eaten by an alligator or is riding the alligator
		for (Alligator a : alligators) {
			if (frog.isBeingEaten(a)) {
				return true;
			}
			if (frog.isRiding(a)) {
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
            switch (screen) {
                case INTRO -> {
                }
                case GAME -> {
                    // Update time spent
                    timerBar.update();
                    // Move frog
                    frog.move(keyPressed);
                    // Check for goal
                    for (Goal goal : goals) {
                        if (frog.isColliding(goal)) {
                            if (goal.fill()) {
                                frog.reset();
                                // add score based on time remaining
                                addScore((int) ((timerBar.getTimeLimit() - timerBar.getTimeSpent()) * 50));
                                // add score based on flies
                                if (goal.hasFly()) {
                                    addScore(100);
                                }
                                
                                boolean allFilled = true;
                                for (Goal g : goals) {
                                    if (!g.isFilled()) {
                                        allFilled = false;
                                        break;
                                    }
                                }
                                
                                if (allFilled) {
                                    nextLevel();
                                }
                                
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
                    // Move turtles
                    for (Turtle t : Turtles) {
                        t.move();
                        if (frog.isRiding(t)) {
                            frog.slide(t);
                        }
                    }
                    // Move alligators
                    for (Alligator a : alligators) {
                        a.move();
                        if (frog.isRiding(a)) {
                            frog.slide(a);
                        }
                    }
                }
				case END -> {
				}
				default -> {
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
			initLevelOne();
		}
		if (screen == END) {
			screen = GAME;
			initLevelOne();
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
			for (Terrain t : grasses) {
				t.draw(g);
			}
			water.draw(g);
			road.draw(g);
			for (Terrain t : barriers) {
				t.draw(g);
			}

			// Draw cars
			for (Car c : cars) {
				c.draw(g);
			}

			// Draw logs
			for (Log l : logs) {
				l.draw(g);
			}

			// Draw turtles
			for (Turtle t : Turtles) {
				t.draw(g);
			}

			// Draw alligators
			for (Alligator a : alligators) {
				a.draw(g);
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
