/*
 * FroggerPanel.java
 * Ario Barin Ostovary
 * This class controls the game's logic
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

class FroggerPanel extends JPanel implements KeyListener, ActionListener, MouseListener {
	public static final int INTRO = 0, GAME = 1, END = 2;
	public static final int WIDTH = 800, HEIGHT = 800;
	private static final int FPS = 60;
	private int screen = INTRO;
	private int score = 0;
	private int highestScore;
	private int level = 1;
	private final LifeCounter lifeCounter;
	private final TimerBar timerBar;

	private final boolean[] keys;
	private final boolean[] keyPressed;
	private final Timer timer;

	private final Frog frog;

	private ArrayList<Car> cars;
	private ArrayList<Log> logs;
	private ArrayList<Turtle> Turtles;
	private ArrayList<Alligator> alligators;

	private ArrayList<Goal> goals;

	private ArrayList<Terrain> grasses;
	private Terrain water;

	private final Font gameFont;

	private final ScoreManager scoreManager;

	private static final BufferedImage gameBackground;

	static {
		gameBackground = Util.loadImage("src/assets/Background/BG.png");
	}

	public FroggerPanel() {
		keys = new boolean[KeyEvent.KEY_LAST + 1];
		keyPressed = new boolean[KeyEvent.KEY_LAST + 1];
		scoreManager = new ScoreManager();
		timerBar = new TimerBar(0, 780, 200, 20, 10, FPS);
		lifeCounter = new LifeCounter(600, 750);
		
		highestScore = scoreManager.getHighestScore();

		// Frog
		frog = new Frog(this, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN);

		gameFont = new Font("Arial", Font.PLAIN, 32);

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
		water = new Terrain(0, 0, WIDTH, HEIGHT / 2 - 50, Terrain.WATER);
		grasses = new ArrayList<>();

		// Goals
		createGoals(75, 50, 5, 150);

		// Terrain
		grasses.add(new Terrain(0, HEIGHT / 2 - 50, WIDTH, 50, Terrain.GRASS));
		grasses.add(new Terrain(0, HEIGHT - 150, WIDTH, 50, Terrain.GRASS));
	}

	private void initLevelOne() {
		initBase();

		// Floating objects
		createLogs(100, 175, 2, false, 2);
		createTurtles(150, 100, 5, true, 4);
		createLogs(200, 200, 5, false, 2);
		createLogs(250, 150, 3, false, 3);
		createTurtles(300, 150, 5, true, 4);

		// Cars
		createCars(400, 125, 5, true, Car.WHITE, 3);
		createCars(450, 50, 4, false, Car.RED, 3);
		createCars(500, 50, 6, true, Car.PINK, 3);
		createCars(550, 50, 3, false, Car.GREEN, 3);
		createCars(600, 50, 4, true, Car.YELLOW, 3);
	}

	private void initLevelTwo() {
		initBase();

		// Floating objects
		createAlligators(100, 200, 2, false, 2);
		createTurtles(150, 100, 5, true, 4);
		createLogs(200, 200, 7, false, 2);
		createLogs(250, 150, 3, false, 3);
		createTurtles(300, 150, 5, true, 4);

		// Cars
		createCars(400, 125, 6, true, Car.WHITE, 3);
		createCars(450, 50, 3, false, Car.RED, 2);
		createCars(500, 50, 5, true, Car.PINK, 4);
		createCars(550, 50, 2, false, Car.GREEN, 3);
		createCars(600, 50, 4, true, Car.YELLOW, 3);
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
		createCars(500, 125, 5, true, Car.RED, 3);
		createCars(550, 50, 9, false, Car.WHITE, 2);
		createCars(600, 50, 6, true, Car.PINK, 6);
		createCars(650, 50, 3, false, Car.GREEN, 4);
		createCars(700, 50, 4, true, Car.YELLOW, 4);
	}

	private void initLevelFour() {
		initBase();

		createTurtles(100, 100, 1, true, 1);
	}

	private void nextLevel() {
		level++;
		frog.reset();
		switch (level) {
			case 2 -> initLevelTwo();
			case 3 -> initLevelThree();
			default -> {
				// Game completed
				screen = END;
			}
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
				// Intro screen logic if any
			}
			case GAME -> {
				// Update time spent
				timerBar.update();

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
					frog.reset();
					timerBar.reset();
					lifeCounter.loseLife();
					if (lifeCounter.isGameOver()) {
						// Game over, prompt for name and save score
						String name = JOptionPane.showInputDialog("Game Over! Enter your name:");
						if (name != null && !name.isEmpty()) {
							String titleCaseName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
							scoreManager.addScore(new Score(titleCaseName, score));
							scoreManager.saveScores();
						}
						screen = END;
					}
				}
				// Move cars
				for (Car c : cars) {
					c.move();
				}

				if (!frog.inAir()) {
					// Reset frog momentum
					frog.resetMomentum();
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

				// Move frog
				frog.move(keyPressed);
			}
			case END -> {
				// End screen logic if any
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
            switch (screen) {
                case INTRO -> {
					screen = GAME;
					score = 0;
					highestScore = scoreManager.getHighestScore();
                    level = 1;
                    lifeCounter.reset();
                    initLevelTwo(); // TEST
                }
                case GAME -> {
                }
                case END -> {
                    screen = INTRO;
                    score = 0;
                    level = 1;
                    lifeCounter.reset();
                    initLevelOne();
                }
            }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void paint(Graphics g) {
		switch (screen) {
			case INTRO -> {
				// Draw Intro Screen
				g.setColor(Color.BLUE);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.WHITE);
				g.setFont(new Font("Arial", Font.BOLD, 48));
				g.drawString("Welcome to Frogger!", 200, 400);
				g.setFont(new Font("Arial", Font.PLAIN, 24));
				g.drawString("Click to Start", 350, 450);
			}
			case GAME -> {
				Util.drawImage(g, gameBackground, 0, 0, WIDTH, HEIGHT);
				// Draw terrain
				for (Terrain t : grasses) {
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
				g.setFont(gameFont);
				g.setColor(Color.WHITE);
				g.drawString("Score: " + score, 50, 700);
			}
			case END -> {
				// Draw End Screen
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.WHITE);
				g.setFont(new Font("Arial", Font.BOLD, 48));
				g.drawString("Game Over", 280, 100);
				g.setFont(new Font("Arial", Font.PLAIN, 36));
				g.drawString("High Scores:", 320, 160);
				// Display high scores
				g.setFont(new Font("Arial", Font.PLAIN, 24));
				ArrayList<Score> highScores = scoreManager.getHighScores();
				int y = 200;
				for (Score s : highScores) {
					g.drawString(s.getName() + ": " + s.getScore(), 340, y);
					y += 30;
				}
				g.setFont(new Font("Arial", Font.PLAIN, 24));
				g.drawString("Click to Restart", 320, y + 50);
			}
		}
	}
}
