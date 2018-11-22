import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Airplane {

    private int x = 1010;
    private int y;
    private int speed;
    private static int width = 100;
    public boolean collided;
    private boolean hitByBullet;

    private Polygon topWing;
    private Polygon headShape;
    private Polygon bottomWing;
    private Rectangle2D.Double body;

    public Airplane(int y, int theSpeed){
        this.y = y;
        this.collided = false;
        speed = theSpeed;
        this.hitByBullet = false;
    }

    public boolean isHitByBullet() {
        return hitByBullet;
    }

    public void setHitByBullet(boolean hitByBullet) {
        this.hitByBullet = hitByBullet;
    }

    public int left(){
        x -= speed;
        if(x+speed < 0) return 1;
        else return 0;
    }

    public Polygon getTopWing() {
        return topWing;
    }

    public Polygon getHeadShape() {
        return headShape;
    }

    public Polygon getBottomWing() {
        return bottomWing;
    }

    public Rectangle2D.Double getBody() {
        return body;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static int getWidth() {
        return width;
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        body = new Rectangle2D.Double(x, y+50, width-1, width/6);

        headShape = new Polygon();
        headShape.addPoint(x, y+50+(width/6));
        headShape.addPoint(x-width/3, y+25+width/3);
        headShape.addPoint(x, y+50);

        topWing = new Polygon();
        topWing.addPoint(x+width-width*3/8, y + 50);
        topWing.addPoint(x+width-width/8, y+width/5);
        topWing.addPoint(x+width-width/8, y+50);

        bottomWing = new Polygon();
        bottomWing.addPoint(x+width-width/4, y+60);
        bottomWing.addPoint(x+width-width/4, y+75);
        bottomWing.addPoint(x+width-width/2+4, y+60);

        g2.setColor(Color.WHITE);
        g2.fill(body);
        g2.setColor(Color.BLACK);
        g2.draw(body);
        g2.setColor(Color.WHITE);
        g2.fill(headShape);
        g2.fill(topWing);
        g2.fill(bottomWing);
        g2.setColor(Color.BLACK);
        g2.draw(headShape);
        g2.draw(topWing);
        g2.draw(bottomWing);
    }
}