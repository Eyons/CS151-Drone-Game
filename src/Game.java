import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements KeyListener {

    private Random random = new Random();
    private Drone drone = new Drone();
    private ArrayList<Airplane> arrayList = new ArrayList<>();
    private Image img;
    private int score;
    private int totalGames;
    private int remainingLives;
    private boolean gameOver;
    private boolean frozen;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private int gameTime;

    private Timer gameTimer = new Timer(1000, e -> {
        if(gameTime != 0)
            gameTime--;
        else{
            endGame();
        }
    });

    private Timer timerSpawn = new Timer(800, e -> {
        int y = random.nextInt(360);
        arrayList.add(new Airplane(y));
    });

    private Timer timer = new Timer(100, e -> {
        ArrayList<Integer> tempCheck = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            int temp = arrayList.get(i).left();
            if (temp == 1) {
                tempCheck.add(i);
            }
        }
        if (tempCheck.size() > 0) {
            for (int removal : tempCheck)
                arrayList.remove(removal);
        }
        detectCollisions();
    });

    public Game() {
        gameTime = 90;
        setFocusable(true);
        addKeyListener(this);

        timerSpawn.start();
        timer.start();
        gameTimer.start();

        this.img = Toolkit.getDefaultToolkit().createImage("background.png");

        score = 0;
        totalGames = 0;
        remainingLives = 3;

        gameOver = false;
        frozen = false;
        up = false;
        down = false;
        left = false;
        right = false;
    }

    private void endGame(){
        timer.stop();
        timerSpawn.stop();
        gameTimer.stop();

        drone.setX(150);
        drone.setY(180);
        frozen = true;
        gameOver = true;

        arrayList.clear();

        totalGames++;
        if(remainingLives > 1)
            score++;
    }

    private void restartGame() {
        gameTime = 90;

        timerSpawn.restart();
        timer.restart();
        gameTimer.restart();

        remainingLives = 3;

        gameOver = false;
        frozen = false;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_UP) && !frozen) {
            drone.up();
            up = true;
        }
        if ((key == KeyEvent.VK_DOWN) && !frozen) {
            drone.down();
            down = true;
        }
        if ((key == KeyEvent.VK_LEFT) && !frozen) {
            drone.left();
            left = true;
        }
        if ((key == KeyEvent.VK_RIGHT) && !frozen) {
            drone.right();
            right = true;
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
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    private void detectCollisions() {
        Rectangle droneTop = new Rectangle(drone.getX(), drone.getY(), drone.getWidth(), drone.getHeight() / 3);
        Rectangle droneBottom = new Rectangle(drone.getX() + drone.getWidth() / 5, drone.getY() + drone.getHeight() / 3, drone.getWidth() - 2 * (drone.getWidth() / 5), drone.getHeight() - (drone.getHeight() / 3));

        for (Airplane airplane : arrayList) {
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
                    break;
                } else {
                    airplane.collided = true;
                    remainingLives -= 1;
                    frozen = true;
                    Timer temp = new Timer(5000, e -> {
                        if(!gameOver)
                            frozen = false;
                    });
                    temp.setRepeats(false);
                    temp.start();
                }
            }

        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, -50, null);
        drone.draw(g);
        for (Airplane airplane : arrayList) {
            airplane.draw(g);
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
        
        repaint();
    }
}
