import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class SmallEnemy extends Enemy {
    
    /**
     * Constructor for SmallEnemy objects, initializing position,
     * bounds, speed, and color.
     * @param frameWidth The width bounds.
     * @param frameHeight The height bounds.
     */
    public SmallEnemy(int frameWidth, int frameHeight) {
        super(new Random().nextInt(frameWidth - 30),
                new Random().nextInt(frameHeight - 30), 30, 30, 6);
        setColor();
    }
    
    /**
     * Sets the given SmallEnemy to a random Color.
     */
    public void setColor() {
        Random rand = new Random();
        // Generate random rgb values.
        setEnemyColor(new Color(rand.nextFloat(),
                rand.nextFloat(), rand.nextFloat()));
    }
    
    /**
     * Computes and updates the next position of the SmallEnemy. The bounds
     * of the enemy must always remain be within the specified frameWidth
     * and frameHeight, otherwise the object will turn around and move down.
     * @param frameWidth The width bounds for movement.
     * @param frameHeight The height bounds for movement.
     */
    public void move(int frameWidth, int frameHeight) {
        int updated = getX() + (int)getEnemySpeed();
        if (updated + getWidth() > frameWidth || updated < 0) {
            setEnemySpeed(getEnemySpeed() * -1); // Turn around enemy.
            // Transition enemy down after hitting a wall.
            transitionY();
        }
        setBounds(updated, getY(),
                getWidth(), getHeight());
    }
    
    /**
     * Shrinks the given SmallEnemy or removes it from the game when
     * hit by a missile.
     * @param list An ArrayList of all current active Enemy objects.
     * @param smallEnemy The index of the given Enemy object within list.
     */
    public void processCollision(ArrayList<Enemy> list, int smallEnemy) {
        radialShrink(list, 30, smallEnemy);
    }
}
