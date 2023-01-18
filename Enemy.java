import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JComponent;

public abstract class Enemy extends JComponent {
    
    private double enemySpeed; // The enemys speed.
    private Color enemyColor; // The enemys color.
    
    /**
     * Sound played when an enemy is defeated.
     */
    private Sound enemyKilled = new Sound("enemyKilled.wav");
    
    /**
     * Sound played when an enemy is hit.
     */
    private Sound enemyHit = new Sound("enemyHit.wav");
    
    /**
     * Constructor for Enemy objects, initializing position,
     * bounds, and speed.
     * @param x The x-component of the Enemys position.
     * @param y The y-component of the Enemys position.
     * @param height The height of the Enemy object.
     * @param width The width of the Enemy object.
     * @param enemySpeed The speed of the Enemy object.
     */
    public Enemy(int x, int y, int height, int width, double enemySpeed) {
        setBounds(x, y, width, height);
        this.enemySpeed = enemySpeed;
    }
    
    /**
     * Determines what occurs when a Missile hits an Enemy.
     * This method is overridden in derived classes.
     * @param list An ArrayList of all current active Enemy objects.
     * @param enemy The index of the given Enemy object within list.
     */
    public abstract void processCollision(ArrayList<Enemy> list, int enemy);
    
    /**
     * A setter method for the color of an Enemy object (set randomly).
     */
    public abstract void setColor();
    
    /**
     * Computes and updates the next position of the Enemy. The bounds
     * of the enemy must always remain be within the specified frameWidth
     * and frameHeight.
     * @param frameWidth The width bounds for movement.
     * @param frameHeight The height bounds for movement.
     */
    public abstract void move(int frameWidth, int frameHeight);
    
    /**
     * Draws a filled circle using the Enemys color and its bounds.
     * @param g A Graphics object for drawing.
     */
    public void paintComponent(Graphics g) {
        g.setColor(enemyColor);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }
    
    /**
     * A getter method for the speed of an Enemy object.
     * @return The speed of the Enemy object.
     */
    public double getEnemySpeed() {
        return enemySpeed;
    }
    
    /**
     * A setter method for the speed of an Enemy object.
     * @param enemySpeed The speed to assign to the Enemy object.
     */
    public void setEnemySpeed(double enemySpeed) {
        this.enemySpeed = enemySpeed;
    }
    
    /**
     * A getter method for the color of an Enemy object.
     * @return The color of the Enemy object.
     */
    public Color getEnemyColor() {
        return enemyColor;
    }
    
    /**
     * A setter method for the color of an Enemy object.
     * @param enemyColor The color to assign to the Enemy object.
     */
    public void setEnemyColor(Color enemyColor) {
        this.enemyColor = enemyColor;
    }
    
    /**
     * Smoothly transitions the referencing enemy to a lower Y-position.
     * Enemies increase speed after each movement downwards.
     */
    public void transitionY() {
        // Create new thread to handle vertical transition.
        Thread t = new Thread() {
            public void run() {
                for (int y = 0; y <= getHeight(); y++) {
                    setBounds(getX(), getY() + 1, getWidth(), getHeight());
                    repaint(); // Apply bound changes.
                    try {
                        // 10ms delay to visualize changes.
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Increase enemy speed with each bounce.
                if (getEnemySpeed() > 0) {
                    setEnemySpeed(getEnemySpeed() + 0.3);
                } else {
                    setEnemySpeed(getEnemySpeed() - 0.3);
                }
            }
        };
        // Starts thread for vertical transition.
        t.start();
    }
    
    /**
     * Smoothly and radially shrinks the given enemy (by 20 for a BigEnemy
     * or by 10 for a SmallEnemy) after collision processing. Plays the
     * appropriate sound after collision detection.
     * @param list An ArrayList of all current active Enemy objects.
     * @param amount The amount by which to shrink the given enemy.
     * @param index The index of the given Enemy object within list.
     */
    public void radialShrink(ArrayList<Enemy> list, int amount, int index) {
        int updatedWidth = getWidth() - amount;
        int updatedHeight = getHeight() - amount;
        startThread(list, amount, index, updatedWidth, updatedHeight);
        if (updatedWidth <= 0 || updatedHeight <= 0) {
            list.remove(index);
        }
    }
    
    /**
     * Starts the thread used in the radialShrink method.
     * @param list An ArrayList of all current active Enemy objects.
     * @param amount The amount by which to shrink the given enemy.
     * @param index The index of the given Enemy object within list.
     * @param updatedWidth The new proposed width for the enemy.
     * @param updatedHeight The new proposed height for the enemy. 
     */
    public void startThread(ArrayList<Enemy> list, int amount, int index,
            int updatedWidth, int updatedHeight) {
        Enemy enemy = list.get(index);
        Thread t = new Thread() {
            public void run() {
                for (int i = 0; i <= amount; i++) {
                    // Center enemy as it shrinks (add half pixel every frame).
                    enemy.setBounds(enemy.getX() + (i % 2 == 0 ? 1 : 0),
                            enemy.getY() + (i % 2 == 0 ? 1 : 0),
                            enemy.getWidth() - 1,
                            enemy.getHeight() - 1);
                    try {
                        // 10ms delay to visualize change.
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Play enemyHit, unless enemy is already defeated.
                if (updatedWidth <= 0 || updatedHeight <= 0) {
                    enemyKilled.play();
                } else {
                    enemyHit.play();
                }
            }
        };
        // Starts thread for enemy shrinking.
        t.start();
    }
}
