import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;

public class Turret extends JComponent {
    
    protected Rectangle base; // The base of the turret.
    protected Rectangle turret; // The gun of the turret.
    private Color turretColor; // The color of the turret.
    private GamePanel panel; // The current GamePanel object.
    private boolean moveLeft; // Flag to tell if the turret is moving left.
    private boolean moveRight; // Flag to tell if the turret is moving right.
    
    /**
     * Constructor for Turret objects, initializing the base, turret,
     * and turretColor.
     * @param base The base of the Turret object.
     * @param turret The gun of the Turret object.
     * @param turretColor The color of the Turret object.
     */
    public Turret(Rectangle base, Rectangle turret, Color turretColor) {
        this.base = base;
        this.turret = turret;
        this.turretColor = turretColor;
    }
    
    /**
     * Flag setter for determining which arrow keys are being held.
     * @param isLeft True when moving left, false when moving right.
     * @param isActive Flag for when arrow keys are held.
     */
    public void setMoveDirection(boolean isLeft, boolean isActive) {
        if (isLeft) {
            moveLeft = isActive;
        } else {
            moveRight = isActive;
        }
    }
    
    /**
     * Moves the turret in the specified direction, ensuring that the object
     * remains within the bounds of the display.
     */
    public void move() {
        int direction = 0;
        if (moveLeft) {
            direction -= 10;
        }
        if (moveRight) {
            direction += 10;
        }
        
        // Move the entire turret based on the specified direction.
        base.setBounds((int)base.getX() + direction, (int)base.getY(),
                (int)base.getWidth(), (int)base.getHeight());
        
        turret.setBounds((int)turret.getX() + direction, (int)turret.getY(),
                (int)turret.getWidth(), (int)turret.getHeight());
        
        // Ensure turret does not cross left wall.
        if (base.getX() <= 0) {
            base.setLocation(0, (int)base.getY());
            turret.setLocation(((int)base.getWidth() / 2)
                    - ((int)turret.getWidth() / 2), (int)turret.getY());
        }
        
        // Ensure turret does not cross right wall.
        if (base.getX() + base.getWidth() >= panel.getWidth()) {
            
            base.setLocation(panel.getWidth() - (int)base.getWidth(),
                    (int)base.getY());
            
            turret.setLocation(panel.getWidth() - ((int)base.getWidth() / 2)
                    - ((int)turret.getWidth() / 2), (int)turret.getY());
        }
    }
    
    /**
     * Setter method for the GamePanel.
     * @param panel The GamePanel object to set.
     */
    public void setGamePanel(GamePanel panel) {
        this.panel = panel;
    }
    
    /**
     * Paints the Turret object's base and barrel with
     * the given turretColor.
     * @param g A Graphics object for drawing.
     */
    public void paintComponent(Graphics g) {
        g.setColor(turretColor);
        g.fillRect((int)base.getX(), (int)base.getY(),
                (int)base.getWidth(), (int)base.getHeight());
        g.fillRect((int)turret.getX(), (int)turret.getY(),
                (int)turret.getWidth(), (int)turret.getHeight());
    }
}
