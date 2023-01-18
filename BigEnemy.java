import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class BigEnemy extends Enemy {
    
    /**
     * Constructor for BigEnemy objects, initializing position,
     * bounds, speed, and color.
     * @param frameWidth The width bounds.
     * @param frameHeight The height bounds.
     */
    public BigEnemy(int frameWidth, int frameHeight) {
        super(new Random().nextInt(frameWidth - 56),
                new Random().nextInt(frameHeight - 56), 56, 56, 4);
        setColor();
    }
    
    /**
     * Sets the given BigEnemy to a random Color.
     */
    public void setColor() {
        Random rand = new Random();
        // Generate random rgb values.
        setEnemyColor(new Color(rand.nextFloat(),
                rand.nextFloat(), rand.nextFloat()));
    }
    
    /**
     * Computes and updates the next position of the BigEnemy. The bounds
     * of the enemy must always remain be within the specified frameWidth
     * and frameHeight, otherwise the object will turn around and move down.
     * @param frameWidth The width bounds for movement.
     * @param frameHeight The height bounds for movement.
     */
    public void move(int frameWidth, int frameHeight) {
        int updated = getX() + (int)getEnemySpeed();
        if (updated + getWidth() > frameWidth || updated < 0) {
            setEnemySpeed(getEnemySpeed() * -1);
            // Transition enemy down after hitting a wall.
            transitionY();
        }
        setBounds(updated, getY(),
                getWidth(), getHeight());
    }
    
    /**
     * Shrinks the given BigEnemy or removes it from the game when
     * hit by a missile.
     * @param list An ArrayList of all current active Enemy objects.
     * @param smallEnemy The index of the given Enemy object within list.
     */
    public void processCollision(ArrayList<Enemy> list, int bigEnemy) {
        radialShrink(list, 28, bigEnemy);
    }
}
