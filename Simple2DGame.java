import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Simple2DGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int playerX, playerY;
    private int xVelocity, yVelocity;
    private final int SPEED = 5;
    private int score = 0;
    private ArrayList<Rectangle> blueSquares;
    private ArrayList<Rectangle> orangeDots;
    private ArrayList<Point> orangeDotsDirections; // To store the directions of orange dots
    private Rectangle purpleBlock;
    private int purpleBlockSpeed = 2;
    private Random random;

    public Simple2DGame() {
        timer = new Timer(1000 / 60, this); // 60 FPS
        timer.start();
        playerX = 50;
        playerY = 50;
        xVelocity = 0;
        yVelocity = 0;
        blueSquares = new ArrayList<>();
        orangeDots = new ArrayList<>();
        orangeDotsDirections = new ArrayList<>(); // Initialize directions list
        random = new Random();
        purpleBlock = new Rectangle(random.nextInt(750), random.nextInt(550), 30, 30);
        setFocusable(true);
        addKeyListener(this);
        generateSquaresAndDots();
    }

    private void generateSquaresAndDots() {
        blueSquares.clear();
        orangeDots.clear();
        orangeDotsDirections.clear(); // Clear directions list
        for (int i = 0; i < 5; i++) {
            blueSquares.add(new Rectangle(random.nextInt(750), random.nextInt(550), 20, 20));
            orangeDots.add(new Rectangle(random.nextInt(750), random.nextInt(550), 20, 20));
            orangeDotsDirections.add(new Point(1, 1)); // Initialize directions to (1,1)
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, 50, 50);

        g.setColor(Color.GREEN);
        for (Rectangle blueSquare : blueSquares) {
            g.fillRect(blueSquare.x, blueSquare.y, blueSquare.width, blueSquare.height);
        }

        g.setColor(Color.ORANGE);
        for (Rectangle orangeDot : orangeDots) {
            g.fillRect(orangeDot.x, orangeDot.y, orangeDot.width, orangeDot.height);
        }

        g.setColor(Color.MAGENTA);
        g.fillRect(purpleBlock.x, purpleBlock.y, purpleBlock.width, purpleBlock.height);

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playerX += xVelocity;
        playerY += yVelocity;
        moveOrangeDots();
        movePurpleBlock();
        checkCollisions();
        repaint();
    }

    private void moveOrangeDots() {
        for (int i = 0; i < orangeDots.size(); i++) {
            Rectangle orangeDot = orangeDots.get(i);
            Point direction = orangeDotsDirections.get(i);

            orangeDot.x += direction.x * SPEED;
            orangeDot.y += direction.y * SPEED;

            // Bounce off walls
            if (orangeDot.x < 0 || orangeDot.x > getWidth() - 20) {
                direction.x = -direction.x;
            }
            if (orangeDot.y < 0 || orangeDot.y > getHeight() - 20) {
                direction.y = -direction.y;
            }

            orangeDotsDirections.set(i, direction);
        }
    }

    private void movePurpleBlock() {
        if (playerX < purpleBlock.x) {
            purpleBlock.x -= purpleBlockSpeed;
        } else if (playerX > purpleBlock.x) {
            purpleBlock.x += purpleBlockSpeed;
        }

        if (playerY < purpleBlock.y) {
            purpleBlock.y -= purpleBlockSpeed;
        } else if (playerY > purpleBlock.y) {
            purpleBlock.y += purpleBlockSpeed;
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < blueSquares.size(); i++) {
            if (new Rectangle(playerX, playerY, 50, 50).intersects(blueSquares.get(i))) {
                score++;
                blueSquares.remove(i);
                break;
            }
        }

        for (Rectangle orangeDot : orangeDots) {
            if (new Rectangle(playerX, playerY, 50, 50).intersects(orangeDot)) {
                score = 0; // Reset score
                generateSquaresAndDots(); // Reset the level
                break;
            }
        }

        if (new Rectangle(playerX, playerY, 50, 50).intersects(purpleBlock)) {
            score -= 5;
            if (score < 0) score = 0;
        }

        if (blueSquares.isEmpty()) {
            generateSquaresAndDots();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                yVelocity = -SPEED;
                break;
            case KeyEvent.VK_DOWN:
                yVelocity = SPEED;
                break;
            case KeyEvent.VK_LEFT:
                xVelocity = -SPEED;
                break;
            case KeyEvent.VK_RIGHT:
                xVelocity = SPEED;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                yVelocity = 0;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                xVelocity = 0;
                break;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple 2D Game");
        Simple2DGame gamePanel = new Simple2DGame();
        frame.add(gamePanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
