import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A class that describes the playing field
 * @author Dmitriy Stepanov
 */
public class GamePanel extends JPanel {
    private final AirHockey game;
	private final Image background_resized;
    private final Puck puck;
    private AI a;
    private final Goal topGoal;
	private final Goal bottomGoal;

    private byte playerScore = 0;
	private byte enemyScore = 0;
    private final ArrayList<MenuButton> menu_buttons;
    private final ArrayList<MenuButton> pause_buttons;

    private Thread collisionThread;
    private final boolean running = false;

    /**
     * Constructor - creating a new playing field
     * @param game - game
     * @param w - width
     * @param h - height
     * @see GamePanel#GamePanel(AirHockey,int,int)
     */
    public GamePanel(final AirHockey game, int w, int h) {
        super();
        this.game = game;

        //Create the main menu buttons
        menu_buttons = new ArrayList<>();
        menu_buttons.add(new MenuButton((game.getWidth() / 2) - 50, 125,
				100, 50, "Regular", new Color(239, 69, 69)));
        menu_buttons.add(new MenuButton((game.getWidth() / 2) - 50, 200,
				100, 50, "Impossible", new Color(239, 69, 69)));

        //Create the pause menu buttons
        pause_buttons = new ArrayList<>();
        pause_buttons.add(new MenuButton((game.getWidth() / 2) - 45, 230,
				100, 35, "Main Menu", new Color(239, 69, 69)));
        pause_buttons.add(new MenuButton((game.getWidth() / 2) - 45, 280,
				100, 35, "Restart", new Color(239, 69, 69)));

        puck = new Puck(game, (game.getWidth() / 2) - 16, (game.getHeight() / 2) - 20);
        topGoal = new Goal((game.getWidth() / 2) - 50, 15);
        bottomGoal = new Goal((game.getWidth() / 2) - 50, game.getHeight() - 65);

        BufferedImage background = AirHockey.loadImage("/cheating.jpeg");
        background_resized = background.getScaledInstance(480, 720, Image.SCALE_DEFAULT);
    }

    public void update() {
        if (game.getGameState() == 3) {
            puck.update();
            game.getPlayer().update(game.getMX(), game.getMY());
            a.update();

            if (topGoal.inGoal(puck)) {
                System.out.println("Goal scored");
                playerScore++;
                goalScored(true);
            } else if (bottomGoal.inGoal(puck)) {
                System.out.println("Goal scored");
                enemyScore++;
                goalScored(false);
            }

            if (playerScore >= 7) {
                goalScored(true);
                game.setGameState(2);
                game.showCursor();
            } else if (enemyScore >= 7) {
                game.setGameState(2);
                game.showCursor();
            }
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (game.getGameState() == 3) {
            g.drawImage(background_resized, 0, 0, null);

            game.getPlayer().render(g);
            puck.render(g);
            a.render(g);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("" + playerScore, game.getWidth() - 30, game.getHeight() - 35);
            g.drawString("" + enemyScore, game.getWidth() - 30, 25);

        } else if (game.getGameState() == 0) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, game.getWidth(), game.getHeight());

            for (MenuButton b : menu_buttons)
                b.render(g, g2, 20);

            g.setFont(new Font("Arial", Font.BOLD, 50));
            int stringWidth = g.getFontMetrics().stringWidth("Air Hockey");
            g.drawString("Air Hockey", (game.getWidth() / 2) - (stringWidth / 2), 70);

        } else if (game.getGameState() == 2) {
            g.setColor(new Color(96, 98, 102, 1));
            g.fillRect(0, 0, game.getWidth(), game.getHeight());
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(155, 155, 200, 200);
            g.setColor(Color.WHITE);
            g.fillRect(150, 150, 200, 200);
            g.setColor(Color.BLACK);

            if (playerScore == 7) {
                g.setFont(new Font("Arial", Font.BOLD, 14));
                g.drawString("Player Won! ", 215, 190);
                g.drawString(playerScore + " - " + enemyScore, 225, 215);
            } else if (enemyScore <= 7) {
                g.setFont(new Font("Arial", Font.BOLD, 14));
                g.drawString("AI Won! ", 215, 190);
                g.drawString(enemyScore + " - " + playerScore, 225, 215);
            }

            for (MenuButton b : pause_buttons)
                b.render(g, g2, 15);

        } else if (game.getGameState() == 4) {
            g.setColor(new Color(96, 98, 102, 1));
            g.fillRect(0, 0, game.getWidth(), game.getHeight());
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(155, 155, 200, 200);
            g.setColor(Color.WHITE);
            g.fillRect(150, 150, 200, 200);
            g.setColor(Color.BLACK);
            g.drawString("Press ESC to continue playing", 165, 180);

            for (MenuButton b : pause_buttons)
                b.render(g, g2, 15);
        }
    }

    //Reset the board, putting the puck on one side or another depending on who scored
    private void goalScored(boolean playerScored) {
        if (playerScored) {
            puck.setX((float) game.getWidth() / 2);
            puck.setY(((float) game.getHeight() / 2) - 200);
        } else {
            puck.setX((float) game.getWidth() / 2);
            puck.setY(((float) game.getHeight() / 2) + 100);
        }

        puck.setVel(0);

        //Move the mouse to the player's side of the board
        try {
            Robot r = new Robot();
            int wx = (int) game.getFrame().getLocation().getX(),
					wy = (int) game.getFrame().getLocation().getY();
            r.mouseMove(wx + game.getWidth() / 2, wy + (game.getHeight() / 2) + 200);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        a.setX(((float) game.getWidth() / 2) - 16);
        a.setY(50);
    }

    //Reset the board in playing game state
    private void reset() {
        playerScore = 0;
        enemyScore = 0;
        puck.setX(((float) game.getWidth() / 2) + 16);
        puck.setY(((float) game.getHeight() / 2));
        a.setX(((float) game.getWidth() / 2) - 16);
        a.setY(50);
        game.setGameState(3);
        game.hideCursor();
    }

    //When a button is clicked, gets a button passed into it from the AirHockey class
    public void clicked(MenuButton b) {
        if (game.getGameState() == 0) {
            if (b.getMessage().equals("Regular")) {
                game.setDifficulty(0);
                a = new AI(((float) game.getWidth() / 2) - 16, 50, 0, game);
                game.setGameState(3);
                game.hideCursor();
            } else if (b.getMessage().equals("Impossible")) {
                game.setDifficulty(1);
                a = new AI(((float) game.getWidth() / 2) - 16, 50, 1, game);
                game.setGameState(3);
                game.hideCursor();
            }
        }

        if (game.getGameState() == 4 || game.getGameState() == 2) {
            if (b.getMessage().equals("Main Menu")) {
                reset();
                game.setGameState(0);
                game.showCursor();
            } else if (b.getMessage().equals("Restart")) {
                game.hideCursor();
                reset();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE) {
            if (game.getGameState() == 3) {
                game.setGameState(4);
                game.showCursor();
            } else if (game.getGameState() == 4) {
                game.setGameState(3);
                game.hideCursor();
            }
        }
    }

    //Combines the menu_buttons and pause_buttons arrays so that you can get every button in the game
    public ArrayList<MenuButton> getButtons() {
        ArrayList<MenuButton> temp = new ArrayList<>();
        temp.addAll(menu_buttons);
        temp.addAll(pause_buttons);
        return temp;
    }

    public ArrayList<MenuButton> getMenuButtons() { return menu_buttons; }
    public ArrayList<MenuButton> getPauseButtons() { return pause_buttons; }
    public Puck getPuck() {
        return puck;
    }
}
