import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Aaron on 11/22/2018.
 */
public class Bullet{

    private int x;
    private int y;
    private static int SPEED = 10;
    private static int WIDTH = 3;
    private static int HEIGHT = 3;
    private int maxTravelDistance;
    private boolean collided;

    public Bullet(Drone drone){
        this.x = drone.getX() + drone.getWidth();
        this.y = drone.getY() + (drone.getHeight()/2);
        this.maxTravelDistance = x + (850/4);
        this.collided = false;
    }

    public int right(){
        if(x >= maxTravelDistance)
            collided = true;
        else x += SPEED;
        if(x+ SPEED > 850) return 1;
        else return 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public boolean isCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        Rectangle2D.Double bullet = new Rectangle2D.Double(x, y, WIDTH, HEIGHT);

        g2.setColor(Color.BLACK);
        g2.fill(bullet);
    }
}
