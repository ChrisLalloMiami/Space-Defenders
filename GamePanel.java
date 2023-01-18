import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class contains the paintable objects such as the enemies, turret, and
 * missile. It also keeps track of the
 * 
 * @author DJ Rao
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
    
    /**
     * The list of enemies in the game. Objects are added in the addEnemy
     * method and removed in the detectCollison method.
     */
    private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    
    /**
     * The list of missiles in the game. Objects are added in the addMissile
     * method and removed in the detectCollison method.
     */
    private ArrayList<Missile> missileList = new ArrayList<Missile>();

    /**
     * The current score in the game. This value is updated in the 
     * detectCollision method.
     */
    private int totalScore;
    
    /**
     * Boolean used to determine the next type of enemy to create.
     * Negated after every enemy creation.
     */
    private boolean isNextEnemyBig;
    
    /**
     * A reference to the current Turret object.
     */
    private Turret turret;
    
    /**
     * Sound played when a missile is fired.
     */
    private Sound sound = new Sound("missileSound.wav");
    
    /**
     * Constructor for the current GamePanel object, initializing the
     * total score, next enemy type, and the Turret object. Also creates
     * a SmallEnemy and a BigEnemy to start the game.
     * @param totalScore The total score to start the game.
     * @param isNextEnemyBig Boolean to determine the next enemy type.
     * @param turret A reference to the current Turret object.
     */
    public GamePanel(int totalScore, boolean isNextEnemyBig, Turret turret) {
        this.totalScore = totalScore;
        this.isNextEnemyBig = isNextEnemyBig;
        this.turret = turret;
        turret.setGamePanel(this);
        enemyList.add(new BigEnemy(100, 100));
        enemyList.add(new SmallEnemy(50, 50));
    }
    
    /**
     * Calls the paintComponent method of the current Turret and
     * all current instances of enemies and missiles.
     * @param g A Graphics object for drawing.
     */
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        turret.paintComponent(g);
        enemyList.forEach(enemy -> enemy.paintComponent(g));
        missileList.forEach(missile -> missile.paintComponent(g));
    }
    
    /**
     * Calls the move method of all current instances of
     * enemies and missiles, as well as the turret.
     */
    public void move() {
        turret.move();
        enemyList.forEach(enemy -> enemy.move(getWidth(), getHeight()));
        for (int i = 0; i < missileList.size(); i++) {
            Missile missile = missileList.get(i);
            missile.move(getWidth(), getHeight(), missileList, i);
        }
    }
    
    /**
     * Adds a new missile to the game at the Turret's position
     * and plays the missileSound.
     */
    public void addMissile() {
        sound.play();
        Rectangle rect = turret.turret;
        Missile missile = new Missile((int)rect.getX()
                + (int)(rect.getWidth() / 2) - 7, (int)rect.getY() - 7);
        missileList.add(missile);
    }
    
    /**
     * Adds either a SmallEnemy or a BigEnemy to the game, depending
     * on the value of isNextEnemyBig.
     */
    public void addEnemy() {
        if (isNextEnemyBig) {
            enemyList.add(new BigEnemy(100, 100));
        } else {
            enemyList.add(new SmallEnemy(50, 50));
        }
        isNextEnemyBig = !isNextEnemyBig;
    }
    
    /**
     * A getter method for the current total score of the game.
     * @return The current total score.
     */
    public int getTotalScore() {
        return totalScore;
    }
    
    /**
     * Method detects the collision of the missile and all the enemies. This is
     * done by drawing invisible rectangles around the enemies and missiles, if
     * they intersect, then they collide.
     * Also triggers user loss when a collision is detected between an enemy
     * and the turret object.
     */
    public void detectCollision() {
        // Uses bounds for enemies and missiles to detect intersection.
        for (int i = 0; i < enemyList.size(); i++) {
            Rectangle enemyRec = enemyList.get(i).getBounds();
            if (enemyRec.intersects(turret.turret)) {
                JOptionPane.showMessageDialog(null, "You Lose!",
                        "Game Finished Message",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            for (int j = 0; j < missileList.size(); j++) {
                Rectangle missileRec = missileList.get(j).getBounds();
                if (missileRec.intersects(enemyRec)) {
                    // Missile has hit an enemy!
                    Enemy enemy = enemyList.get(i);
                    enemy.processCollision(enemyList, i);
                    missileList.remove(j);
                    if (enemy instanceof BigEnemy) {
                        totalScore += 100;
                    } else {
                        totalScore += 150;
                    }
                }
            }
        }
    }
}
