/* PongPanel.java
 *
 **/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class FroggerPanel extends JPanel implements KeyListener, ActionListener, MouseListener {
	public static final int INTRO = 0, GAME = 1, END = 2;
	private static final int WIDTH = 800, HEIGHT = 800;
	private int screen = GAME; // Change to intro
	private int score;

	private boolean[] keys;
	private boolean[] processedKeys;
	Timer timer;
	private Frog frog;
	private Font fontComic; // Change font

	public FroggerPanel() {
		keys = new boolean[KeyEvent.KEY_LAST + 1];
		processedKeys = new boolean[KeyEvent.KEY_LAST + 1];
		frog = new Frog(this, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN);
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

	public void move() {
		if (screen == INTRO) {

		} else if (screen == GAME) {
			frog.move(keys);
		}

	}

	public void addScore(int side) {
		score += 100;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		move();
		repaint();

	}

	@Override
	public void keyReleased(KeyEvent ke) {
		int key = ke.getKeyCode();
		keys[key] = false;
		processedKeys[key] = false;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		int key = ke.getKeyCode();
		if (!processedKeys[key]) {
			keys[key] = true;
			processedKeys[key] = true;
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
			frog.draw(g);
			g.setFont(fontComic);
			g.drawString("" + score, 50, 700);
		}
	}
}
