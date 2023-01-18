import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * The top-level Frame class class for the Game.
 * 
 * @author DJ Rao
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame {
    
    /** Fixed width for the game. */
    private static final int WINDOW_WIDTH = 700;
    /** Fixed height for the game. */
    private static final int WINDOW_HEIGHT = 500;

    /**
     * Just a label added to the north of the game panel to display score.
     * 
     * @see score
     */
    private JLabel scoreLabel;

    /**
     * The remaining number of calls to the gameStep method before a new enemy
     * is automatically added to the game.
     */
    private int enemyGenerationCounter;

    /**
     * The number of missile objects that have been created.
     */
    private int shotsFired = 0;

    /**
     * A button that allows the user to fire a missile.
     */
    private JButton fireButton;

    /**
     * A static reference to the current GameFrame.
     */
    private static GameFrame gameFrame;
    
    /**
     * The game panel that logically encapsulates all the enemies, missiles, and
     * the turret.
     */
    private GamePanel panel;

    /**
     * Default constructor to control the game.
     */
    public GameFrame() {
        
        // Setup the scoreLabel.
        scoreLabel = new JLabel("0");
        scoreLabel.setForeground(Color.BLACK);
        // Setup the fireButton.
        fireButton = new JButton("Fire! (Space)");
        fireButton.setBackground(Color.RED);
        // Listen for mouse clicks and fire a missile.
        fireButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.addMissile();
                shotsFired++;
                GameFrame.gameFrame.requestFocus();
            }
        });
        
        // Setup the Turret.
        Random rand = new Random();
        Color turretColor = new Color(rand.nextFloat(),
                rand.nextFloat(), rand.nextFloat());
        
        Rectangle base = new Rectangle(WINDOW_WIDTH / 2 - 35,
                WINDOW_HEIGHT - 45, 70, 30);
        
        Rectangle gun = new Rectangle(WINDOW_WIDTH / 2 - 11,
                WINDOW_HEIGHT - 100, 22, 80);
        
        Turret turret = new Turret(base, gun, turretColor);
        
        // Setup the GamePanel.
        panel = new GamePanel(0, rand.nextInt(2) == 1 ? true : false, turret);
        // Setup the initial JFrame elements
        setTitle("Ball Destruction!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setFocusable(true);
        setLayout(new BorderLayout());
        
        // Handle user input and set move direction appropriately.
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    turret.setMoveDirection(true, true);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    turret.setMoveDirection(false, true);
                }
                // Add functionality for firing with space.
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    panel.addMissile();
                    shotsFired++;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // Unused.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Adjust movement flags on key release.
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    turret.setMoveDirection(true, false);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    turret.setMoveDirection(false, false);
                }
            }
        });
        

        // Setup rest of the GUI

        // Ensure you uncomment the following line when ready
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); 
        this.add(scoreLabel, BorderLayout.PAGE_START);
        this.add(fireButton, BorderLayout.PAGE_END);
        this.add(panel);
        this.pack();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // For basic operation, you don't need to modify below this line. 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /**
     * This method must be called to start operations of the game. This method
     * essentially just creates a simple timer that calls the gameStep method
     * every 30 milliseconds.
     */
    public void start() {
        // Center the frame on the screen and show it.
        centerFrame(this);
        setVisible(true);
        // Create a game-step timer to step through the game.
        Timer gameStep = new Timer(30, e -> gameStep());
        gameStep.start();
    }

    /**
     * Method to perform one step in the animation of the game. This method
     * essentially performs the following tasks:
     * 
     * <ol>
     * <li>Performs operations associated with detecting collisions between
     * enemy(s) and missile(s) by calling GamePanel.detectCollision()</li>
     * <li>Updates score in the display label</li>
     * <li>Moves all the enemies and missiles and repaints the panel to update
     * the display.</li>
     * <li>If the number of shots fired is greater-than 10, it displays a
     * suitable message and stops the game by calling System.exit(0)</li>
     * </ol>
     */
    private void gameStep() {
        panel.detectCollision();
        int score = panel.getTotalScore();
        scoreLabel.setText(Integer.toString(score));
        panel.move();
        panel.repaint();
        if (shotsFired > 10) {
            if (score >= 800) {
                JOptionPane.showMessageDialog(null, "You Win!",
                        "Game Finished Message",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "You Lose!",
                        "Game Finished Message",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
        if (enemyGenerationCounter == 0) {
            panel.addEnemy();
            setEnemyGenerationCounter();
        }
        enemyGenerationCounter--;
    }

    /**
     * Method centers the frame in the middle of the screen. This is really
     * just a nicety and does not affect the operation of the game in any
     * meaningful way.
     * 
     * @param frame to center with respect to the users screen dimensions.
     */
    private void centerFrame(JFrame frame) {
        int width = frame.getWidth();
        int height = frame.getHeight();
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        Point center = ge.getCenterPoint();
        // Figure out where the frame needs to be positions to center it.
        int xposition = center.x - width / 2;
        int yposition = center.y - height / 2;
        // Center the frame.
        frame.setBounds(xposition, yposition, width, height);
        frame.validate();
    }

    /**
     * Randomly assign a value to determine how soon a new Enemy should be
     * created.
     */
    private void setEnemyGenerationCounter() {
        // Set the number of times the gameStep method should be called
        // before the next enemy is added to the game.
        enemyGenerationCounter = (int) (Math.random() * 300);
    }

    /**
     * The main method to execute the game.
     * 
     * @param args Command-line arguments if any. This program does not use this
     *             argument.
     */
    public static void main(String[] args) {
        gameFrame = new GameFrame();
        gameFrame.start();
        
    }
}
