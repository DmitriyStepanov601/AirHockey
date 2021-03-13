import java.awt.*;

/**
 * Class describing a goal
 * @author Dmitriy Stepanov
 */
public class Goal {
    private final Rectangle bounds;

    /**
     * Constructor - creating a new goal
     * @param x - X coordinate
     * @param y - Y coordinate
     * @see Goal#Goal(int,int)
     */
    public Goal(int x, int y) {
        this.bounds = new Rectangle(x, y, 100, 10);
    }

    public void render(Graphics2D g) {
        g.setColor(Color.RED);
        g.draw(bounds);
    }

    public Rectangle getBounds() {
        return bounds;
    }
    public boolean inGoal(Puck p) { //returns true/false depending if the point passed is inside the goal
        return bounds.intersects(p.getBounds());
    }
}
