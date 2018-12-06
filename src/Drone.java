import java.awt.*;

public class Drone {

    private double x = 150;
    private int y = 180;
    private double updatedX = 0;
    private int width = 46;
    private int height = 18;
    private Image img;

    public Drone(){
    this.img = Toolkit.getDefaultToolkit().createImage("drone.png");
    // Same comment as the one located in Game Constructor
//        this.img = Toolkit.getDefaultToolkit().createImage(getClass().getResource("drone.png"));
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getDX(){
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void up(){
        if(y > 0) y -= 15;
    }

    public void down(){
        if(y < 340) y += 15;
    }

    public void left(){
        if(x > 0) x -= 15;
    }

    public void right(){
        if(x < 804) x += 15;
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, (int)x, y,null);
    }
}
