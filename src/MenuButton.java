import java.awt.*;

/**
 * A class that describes the menu buttons
 * @author Dmitriy Stepanov
 */
public class MenuButton {
    private final String message;
    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private final Color c;
    private final Rectangle bounds;
    private boolean hovered = false;

    /**
     * Constructor - creating new menu buttons
     * @param x - X coordinate
     * @param y - Y coordinate
     * @param w - width
     * @param h - height
     * @param message - text message
     * @param c - color
     * @see MenuButton#MenuButton(int,int,int,int,String,Color)
     */
    public MenuButton(int x, int y, int w, int h, String message, Color c) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.message = message;
        this.c = c;
        this.bounds = new Rectangle(x, y + 10, w, h);
    }

    public void render(Graphics g, Graphics2D g2, int fontSize) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x + 5, y + 5, w, h);

        if (hovered)
            g.setColor(c.brighter());
        else
            g.setColor(c);

        g.fillRect(x, y, w, h);
        g.setColor(Color.GRAY);
        g.drawRect(x, y, w, h);

        int messageWidth = g.getFontMetrics().stringWidth(message);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        if (message.equals("Regular"))
            g.drawString(message, x + 20, y + (h / 2) + 6);
        else
            g.drawString(message, x + ((w / 2) - (messageWidth / 2)), y + (h / 2) + 6);
    }

    public Rectangle getBounds() {
        return bounds;
    }
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
    public String getMessage() {
        return message;
    }
}
