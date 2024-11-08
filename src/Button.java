/*
 * Button.java
 * Ario Barin Ostovary
 * This class controls the buttons' drawing, hovering, and clicking.
 */

import java.awt.*;
import java.awt.event.MouseEvent;

public class Button {
    private final int x, y, width, height;
    private final String text; // Text on the button
    private boolean hovered = false;
    private final Color color;
    private final Color hoverColor;

    public Button(String text, int x, int y, int width, int height, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;

        // Half transparency for hover effect
        hoverColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 2);
    }

    public void draw(Graphics g) {
        // Draw the button - hovered or not
        g.setColor(hovered ? hoverColor : color);
        g.fillRect(x, y, width, height);

        // Draw the text
        Util.writeCenteredText(g, text, x + width / 2, y + height / 2, 20, Util.WHITE_FONT);
    }

    public boolean isHovered(MouseEvent e) {
        // Check if the mouse is hovering over the button
        return e.getX() >= x && e.getX() <= x + width && e.getY() >= y && e.getY() <= y + height;
    }

    public void setHovered(boolean hovered) {
        // Set the hovered state
        this.hovered = hovered;
    }

    public boolean isClicked(MouseEvent e) {
        // Check if the button is clicked
        return isHovered(e);
    }
} 