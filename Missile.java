import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JComponent;

public class Missile extends JComponent {

    private int missileSpeed; // The missiles speed.
    private Color missileColor; // The missiles color.
    
    /**
     * Constructor for Missile objects, initializing
     * position, bounds, and speed.
     * @param x The x-component of the Missiles position.
     * @param y The y-component of the Missiles position.
     */
    public Missile(int x, int y) {
        setBounds(x, y, 15, 15);
        missileSpeed = 5;
        setMissileColor();
    }
    
    /**
     * Draws a filled circle using the Missiles color and its bounds.
     * @param g A Graphics object for drawing.
     */
    public void paintComponent(Graphics g) {
        g.setColor(missileColor);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }
    
    /**
     * Sets the given Missile to a random Color.
     */
    public void setMissileColor() {
        Random rand = new Random();
        // Generate random rgb values.
        missileColor = new Color(rand.nextFloat(),
                rand.nextFloat(), rand.nextFloat());
    }
    
    /**
     * A getter method for the speed of a Missile object.
     * @return The speed of the Missile object.
     */
    public int getMissileSpeed() {
        return missileSpeed;
    }
    
    /**
     * Computes and updates the next position of the Missile.
     * If the missile goes off panel, it will be removed from
     * the game.
     * @param panelWidth The width bounds for movement.
     * @param panelHeight The height bounds for movement.
     * @param list An ArrayList of all current active Missile objects.
     * @param missile The index of the given Missile object within list.
     */
    public void move(int panelWidth, int panelHeight,
            ArrayList<Missile> list, int missile) {
        if (getY() >= panelHeight || getY() <= 0) {
            list.remove(missile);
        }
        int updated = getY() - missileSpeed;
        setBounds(getX(), updated, getWidth(), getHeight());
    }
}
