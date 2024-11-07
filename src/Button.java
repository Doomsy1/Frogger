import java.awt.*;
import java.awt.event.MouseEvent;

public class Button {
    private final int x, y, width, height;
    private final String text;
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
        hoverColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 2);
    }

    public void draw(Graphics g) {
        g.setColor(hovered ? hoverColor : color);
        g.fillRect(x, y, width, height);
        Util.writeCenteredText(g, text, x + width / 2, y + height / 2, 20, Util.WHITE_FONT);
    }

    public boolean isHovered(MouseEvent e) {
        return e.getX() >= x && e.getX() <= x + width && e.getY() >= y && e.getY() <= y + height;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isClicked(MouseEvent e) {
        return isHovered(e);
    }
} 