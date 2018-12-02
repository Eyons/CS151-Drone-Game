import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends JPanel implements KeyListener {

    private Random random = new Random();
    private Drone drone = new Drone();

    private ArrayList<Airplane> airplanes = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();

    private Image img;
    private Image img2;

    private int score;
    private int totalGames;
    private int remainingLives;

    private boolean gameOver;
    private boolean frozen;

    private boolean haveBullet;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean spaceBar;

    private int gameTime;
    private int planeSpeed = 10;
    private int collisionSec = 5;
    private int imgPosition = 0;
    private int img2Position = 850;

    private Timer releaseFrozen = new Timer(5000, e -> {
        if(!gameOver)
            frozen = false;
    });

    private Timer gameTimer = new Timer(1000, e -> {
        if(gameTime != 0)
            gameTime--;
        else{
            endGame();
        }
    });
    
    private Timer backgroundScroll = new Timer(100, e -> {
        if(!up && !down && !left && !right){
            drone.setX(drone.getX()+2);
        }

    	if(imgPosition > -850 || img2Position > 0)
    	{
    		if(!frozen)
    		{
                imgPosition-= planeSpeed/5;
                img2Position-= planeSpeed/5;
    		}
            
    	}
    	else
    	{
    		imgPosition = 0;
    		img2Position = 850;
    	}

    });
    
    
    private Timer collisionTimer = new Timer(1000, e -> {
        if(collisionSec > 0 && frozen)
            collisionSec--;

    });
    
    private Timer timerSpawn = new Timer(1000, e -> {
		
        int y = random.nextInt(360);
        planeSpeed = ThreadLocalRandom.current().nextInt(5, 16);

        airplanes.add(new Airplane(y,planeSpeed));
    });

    private Timer timerShoot = new Timer(2500, e -> {
        haveBullet = true;
    });

    private Timer timer = new Timer(100, e -> {
        detectCollisions();
        // If we want to clean the playing field during the game, do it here
        // be careful for null pointer exceptions though.
        if(spaceBar && haveBullet) {
            haveBullet = false;
            timerShoot.restart();
            bullets.add(new Bullet(drone));
        }

        for(int i = 0; i < bullets.size(); i++){
            bullets.get(i).right();
        }
        for (int i = 0; i < airplanes.size(); i++) {
            airplanes.get(i).left();
        }
        detectCollisions();
    });
    
    public Game() {
        random.setSeed(System.currentTimeMillis());
        gameTime = 90;
        setFocusable(true);
        addKeyListener(this);

        timerShoot.start();
        timerSpawn.start();
        timer.start();
        gameTimer.start();
        collisionTimer.start();
        backgroundScroll.start();

        releaseFrozen.setRepeats(false);

	// Grabs the images from the "res" folder in the main directory
//        this.img = Toolkit.getDefaultToolkit().createImage(getClass().getResource("background.png"));
//        this.img2 = Toolkit.getDefaultToolkit().createImage(getClass().getResource("background.png"));
	    
        this.img = Toolkit.getDefaultToolkit().createImage("background.png");
        this.img2 = Toolkit.getDefaultToolkit().createImage("background.png");

        score = 0;
        totalGames = 0;
        remainingLives = 3;

        gameOver = false;
        frozen = false;
        up = false;
        down = false;
        left = false;
        right = false;
        haveBullet = true;
    }

    private void endGame(){
        timer.stop();
        timerSpawn.stop();
        gameTimer.stop();
        timerShoot.stop();

        drone.setX(150);
        drone.setY(180);
        frozen = true;
        gameOver = true;

        airplanes.clear();
        bullets.clear();

        totalGames++;
        if(remainingLives > 1)
            score++;
    }
    

    private void restartGame() {
        random.setSeed(System.currentTimeMillis());
        gameTime = 90;
        collisionSec = 5;

        timerShoot.restart();
        timerSpawn.restart();
        timer.restart();
        gameTimer.restart();

        remainingLives = 3;

        gameOver = false;
        frozen = false;
        haveBullet = false;
    }
    

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_UP) && !frozen) {
            up = true;
        }
        if ((key == KeyEvent.VK_DOWN) && !frozen) {
            down = true;
        }
        if ((key == KeyEvent.VK_LEFT) && !frozen) {
            left = true;
        }
        if ((key == KeyEvent.VK_RIGHT) && !frozen) {
            right = true;
        }
        if ((key == KeyEvent.VK_SPACE) && !frozen){
            spaceBar = true;
        }
        if((up && left) && !frozen){
            drone.up();
            drone.left();
        }
        if((up && right) && !frozen){
            drone.up();
            drone.right();
        }
        if((down && left) && !frozen){
            drone.down();
            drone.left();
        }
        if((down && right) && !frozen){
            drone.down();
            drone.right();
        }

        if((!up && left) && !frozen){
            drone.left();
        }
        if((up && !left) && !frozen){
            drone.up();
        }

        if((!up && right) && !frozen){
            drone.right();
        }
        if((up && !right) && !frozen){
            drone.up();
        }

        if((!down && left) && !frozen){
            drone.left();
        }
        if((down && !left) && !frozen){
            drone.down();
        }

        if((!down && right) && !frozen){
            drone.right();
        }
        if((down && !right) && !frozen){
            drone.down();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            up = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            down = false;
        }
        if (key == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if(key == KeyEvent.VK_SPACE && gameOver){
            restartGame();
            spaceBar = false;
        } else if(key == KeyEvent.VK_SPACE){
            spaceBar = false;
        }
        
    }

    public void keyTyped(KeyEvent e) {
    }

    private void detectCollisions() {
        Rectangle droneTop = new Rectangle(drone.getX(), drone.getY(), drone.getWidth(), drone.getHeight() / 3);
        Rectangle droneBottom = new Rectangle(drone.getX() + drone.getWidth() / 5, drone.getY() + drone.getHeight() / 3, drone.getWidth() - 2 * (drone.getWidth() / 5), drone.getHeight() - (drone.getHeight() / 3));

        for (Airplane airplane : airplanes) {
            for(Bullet bullet : bullets){
                if(!bullet.isCollided()) {
                    Rectangle bulletRect = new Rectangle(bullet.getX(), bullet.getY(), bullet.getWIDTH(), bullet.getHEIGHT());
                    // Bullet collision is off for the wings at bullet speed 20, at 10 works fine mostly
                    if (((airplane.getTopWing().intersects(bulletRect))
                            || (airplane.getBottomWing().intersects(bulletRect))
                            || (airplane.getHeadShape().intersects(bulletRect))
                            || (airplane.getBody().intersects(bulletRect)))
                            && !airplane.collided) {
                        airplane.collided = true;
                        airplane.setHitByBullet(true);
                        bullet.setCollided(true);
                    }
                }
            }
            // Checking for intersection via Polygons isn't as accurate as checking
            // for every single line, don't edit below collision code
            if ((droneTop.intersects(airplane.getX(), airplane.getY() + 50, airplane.getWidth() - 1, airplane.getWidth() / 6)
                    || droneTop.intersectsLine(airplane.getX(), airplane.getY()+50+(airplane.getWidth()/6), (airplane.getX()-airplane.getWidth()/3), airplane.getY()+25+airplane.getWidth()/3)
                    || droneTop.intersectsLine(airplane.getX(), airplane.getY()+50, (airplane.getX()-airplane.getWidth()/3), airplane.getY()+25+airplane.getWidth()/3)
                    || droneTop.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()*3/8, airplane.getY() + 50, airplane.getX()+airplane.getWidth()-airplane.getWidth()/8, airplane.getY()+airplane.getWidth()/5)
                    || droneTop.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()/8, airplane.getY()+airplane.getWidth()/5, airplane.getX()+airplane.getWidth()-airplane.getWidth()/8, airplane.getY()+50)
                    || droneTop.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()/4, airplane.getY()+60, airplane.getX()+airplane.getWidth()-airplane.getWidth()/4, airplane.getY()+75)
                    || droneTop.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()/2+4, airplane.getY()+60, airplane.getX()+airplane.getWidth()-airplane.getWidth()/4, airplane.getY()+75)
                    || droneTop.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()/4, airplane.getY()+60, airplane.getX()+airplane.getWidth()-airplane.getWidth()/2+4, airplane.getY()+60)
                    || droneBottom.intersects(airplane.getX(), airplane.getY() + 50, airplane.getWidth() - 1, airplane.getWidth() / 6)
                    || droneBottom.intersectsLine(airplane.getX(), airplane.getY()+50+(airplane.getWidth()/6), (airplane.getX()-airplane.getWidth()/3), airplane.getY()+25+airplane.getWidth()/3)
                    || droneBottom.intersectsLine(airplane.getX(), airplane.getY()+50, (airplane.getX()-airplane.getWidth()/3), airplane.getY()+25+airplane.getWidth()/3)
                    || droneBottom.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()*3/8, airplane.getY() + 50, airplane.getX()+airplane.getWidth()-airplane.getWidth()/8, airplane.getY()+airplane.getWidth()/5)
                    || droneBottom.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()/8, airplane.getY()+airplane.getWidth()/5, airplane.getX()+airplane.getWidth()-airplane.getWidth()/8, airplane.getY()+50)
                    || droneBottom.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()/4, airplane.getY()+60, airplane.getX()+airplane.getWidth()-airplane.getWidth()/4, airplane.getY()+75)
                    || droneBottom.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()/2+4, airplane.getY()+60, airplane.getX()+airplane.getWidth()-airplane.getWidth()/4, airplane.getY()+75)
                    || droneBottom.intersectsLine(airplane.getX()+airplane.getWidth()-airplane.getWidth()/4, airplane.getY()+60, airplane.getX()+airplane.getWidth()-airplane.getWidth()/2+4, airplane.getY()+60))
                    && !airplane.collided) {
                if (remainingLives == 1) {
                    timer.stop();
                    timerSpawn.stop();
                    remainingLives--;
                    gameOver = true;
                    frozen = true;
                    endGame();
                    planeSpeed = 10;
                    break;
                } else {
                    releaseFrozen.restart();
                    airplane.collided = true;
                    remainingLives -= 1;
                    frozen = true;
                    collisionSec = 5;
                    collisionTimer.restart();
                    releaseFrozen.start();
                }
                
       
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, imgPosition, -50, null);
        g.drawImage(img2, img2Position, -50, null);
        drone.draw(g);
        for (Airplane airplane : airplanes) {
            if(!airplane.isHitByBullet())
                airplane.draw(g);
        }
        for(Bullet bullet : bullets){
            if(!bullet.isCollided())
                bullet.draw(g);
        }
        
        String timeFormat = String.format("%1d:%02d", (gameTime/60), (gameTime-(gameTime/60)*60));
        g.drawString("Score: " + score + " out of " + totalGames, 380, 325);
        g.drawString("Remaining Lives: " + remainingLives, 375, 340);
        g.drawString("Remaining Time: " + timeFormat, 360, 355);
        if (gameOver) {
            g.setFont(new Font("", Font.BOLD, 50));
            g.drawString("Press Spacebar", 250, 40);
            g.drawString("to start a new game", 200, 85);
        }
        
        if (frozen && !gameOver) {
            g.setFont(new Font("", Font.BOLD, 50));
            g.drawString("Collision Detected", 225, 40);
            String collisionTimeDrawn = String.format("%01d", collisionSec);
            g.drawString(collisionTimeDrawn, 450, 100);
        }
        
        repaint();
    }
}
