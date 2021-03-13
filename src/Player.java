import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * A class that describes the player
 * @author Dmitriy Stepanov
 */
public class Player {
    private int x;
    private int y;
    private final int radius;
    private float velx, vely;
    private final AirHockey game;
    private final float SL = 5;

    /**
     * Constructor - creating a new player
     * @param game - game
     * @param x - X coordinate
     * @param y - Y coordinate
     * @param radius - radius
     * @see Player#Player(AirHockey,int,int,int)
     */
    public Player(AirHockey game, int x, int y, int radius) {
        this.radius = radius / 2;
        this.x = x;
        this.y = y;
        this.game = game;
    }

    public void update(int mx, int my) {
        //Gets the x and y pos before the update, then calculates the x/y velocity after the update
        //Sets the x and y pos to the mousex and mousey position
        int startx = x, starty = y;

        if ((mx - radius) - 5 <= 15)
            this.x = 15;
        else if (mx + radius >= game.getWidth() - 30)
            this.x = (game.getWidth() - 30) - radius * 2;
        else
            this.x = mx - 27;

        if (my < (game.getHeight() / 2) + 30)
            this.y = (game.getHeight() / 2) - 14;
        else if (my >= game.getHeight() - 54)
            this.y = (game.getHeight() - 54) - radius * 2;
        else
            this.y = my - 45;

        //Velocity calculation, scaled down
        velx = (float) ((x - startx) * 0.75);
        vely = (float) ((y - starty) * 0.75);
    }

    //Renders the player
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);             // Antialiasing for smooth circles

        //First ellipse
        Ellipse2D s = new Ellipse2D.Float();
        g.setColor(new Color(0, 153, 255));
        s.setFrame(x, y, radius * 2, radius * 2);
        g2.fill(s);

        //Draws a second ellipse on top of the other
        Ellipse2D s2 = new Ellipse2D.Float();
        g.setColor(new Color(0, 51, 255));
        s2.setFrame(x + ((float)radius / 2), y + ((float)radius / 2), radius, radius);
        g2.fill(s2);
    }

    public int getRadius() {
        return radius;
    }
    public int getX() {
        return x + radius;
    }
    public int getY() {
        return y + radius;
    }
    public float getVelX() {
        return Math.abs(velx);
    }
    public float getVelY() {
        return Math.abs(vely);
    }
    public double getMass() { return 1.5; }
}
