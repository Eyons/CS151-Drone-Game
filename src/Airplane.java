import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Airplane {

    private int x = 1010;
    private int y;
    private int speed = 20;
    private static int width = 100;
    public boolean collided;

    public Airplane(int y){
        this.y = y;
        this.collided = false;
    }

//    public int right(){
//        x += speed;
//        if(x+speed > 850)
//            return 1;
//        else
//            return 0;
//    }

    public int left(){
        x -= speed;
        if(x+speed < 0) return 1;
        else return 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public static int getWidth() {
        return width;
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Rectangle2D.Double body = new Rectangle2D.Double(x, y+50, width-1, width/6);
//        Line2D.Double head = new Line2D.Double(x, y+50+(width/6), (x-width/3), y+25+width/3);
//        Line2D.Double head2 = new Line2D.Double(x, y+50, (x-width/3), y+25+width/3);
//        Line2D.Double wing = new Line2D.Double(x+width-width*3/8, y + 50, x+width-width/8, y+width/5);
//        Line2D.Double wing2 = new Line2D.Double(x+width-width/8, y+width/5, x+width-width/8, y+50);
//        Line2D.Double wing3 = new Line2D.Double(x+width-width/4, y+60, x+width-width/4, y+75);
//        Line2D.Double wing4 = new Line2D.Double(x+width-width/2+4, y+60, x+width-width/4, y+75);
//        Line2D.Double wing5 = new Line2D.Double(x+width-width/4, y+60, x+width-width/2+4, y+60);

        Polygon headShape = new Polygon();
        headShape.addPoint(x, y+50+(width/6));
        headShape.addPoint(x-width/3, y+25+width/3);
        headShape.addPoint(x, y+50);

        Polygon topWing = new Polygon();
        topWing.addPoint(x+width-width*3/8, y + 50);
        topWing.addPoint(x+width-width/8, y+width/5);
        topWing.addPoint(x+width-width/8, y+50);

        Polygon bottomWing = new Polygon();
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

//        g2.draw(head);
//        g2.draw(head2);
//
//        g2.draw(wing);
//        g2.draw(wing2);
//
//        g2.draw(wing3);
//        g2.draw(wing4);
//        g2.draw(wing5);
    }
}
