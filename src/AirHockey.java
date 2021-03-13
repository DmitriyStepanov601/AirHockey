import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A class that describes the gameplay
 * @author Dmitriy Stepanov
 */
public class AirHockey implements Runnable {
    private final JFrame frame;
    private final GamePanel panel;
    private final Player player;

    private int gameState = 0;       //0 = main menu, 1 = difficulty menu, 2 = Game over/Score,
                                     // 3 = game, 4 = paused
    private int difficulty = 0;      //0 = easy, 1 = hard
    private final int WIDTH = 480;
    private final int HEIGHT = 749;
    private int mx = 0;
    private int my = 0;
    private final boolean running;
    private final Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    /**
     * Constructor - creating a new game
     * @see AirHockey#AirHockey()
     */
    public AirHockey() {
        frame = new JFrame("Air Hockey");
        player = new Player(this, 200, 500, 40);
        panel = new GamePanel(this, WIDTH, HEIGHT);

        Thread updateThread = new Thread(this);
        Image windowIcon = loadImage("/airhockey.png");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setIconImage(windowIcon);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        //Constantly gets the mouse x position and mouse y position
        frame.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                mx = e.getX();
                my = e.getY();

                if (gameState == 0 || gameState == 4 || gameState == 2) {
                    for (MenuButton b : panel.getButtons())
                        b.setHovered(b.getBounds().contains(mx - 5, my - 20));
                }
            }
        });

        //Finds out which button was pressed and passes it to the panel
        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (gameState == 0) {
                    for (MenuButton b : panel.getMenuButtons())
                        if (b.getBounds().contains(mx - 5, my - 20))
                            panel.clicked(b);
                } else if (gameState == 4 || gameState == 2) {
                    for (MenuButton b : panel.getPauseButtons())
                        if (b.getBounds().contains(mx - 5, my - 20))
                            panel.clicked(b);
                }
            }
        });

        //Passes the keyEvent to the panel
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                panel.keyPressed(e);
            }
        });
        running = true;
        updateThread.start();
        frame.add(panel);
        frame.setVisible(true);
    }

    public void run() {
        while (running) {
            panel.update();
            try {
                int FPS = 60;
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static BufferedImage loadImage(String pathImage) {
        try {
            return ImageIO.read(AirHockey.class.getResource(pathImage));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }
    public int getGameState() {
        return gameState;
    }
    public int getMX() {
        return mx;
    }
    public int getMY() {
        return my;
    }
    public int getWidth() {
        return WIDTH;
    }
    public int getHeight() {
        return HEIGHT;
    }
    public GamePanel getPanel() {
        return panel;
    }
    public void setGameState(int gameState) {
        this.gameState = gameState;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    public int getDifficulty() {
        return difficulty;
    }
    public JFrame getFrame() {
        return frame;
    }

    //Hides the cursor
    public void hideCursor() {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg,
                new Point(0, 0), "blank cursor");
        frame.getContentPane().setCursor(blankCursor);
    }

    //Shows the cursor
    public void showCursor() {
        frame.getContentPane().setCursor(Cursor.getDefaultCursor());
    }

    public static void main(String[] args) {
        new AirHockey();
    }
}
