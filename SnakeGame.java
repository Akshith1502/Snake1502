import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeGame extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 400;
    private final int BOARD_HEIGHT = 400;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = 400;
    private final int RAND_POS = 20;
    private final int DELAY = 150;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int snakeSize;
    private int appleX;
    private int appleY;

    private boolean isLeft = false;
    private boolean isRight = true;
    private boolean isUp = false;
    private boolean isDown = false;
    private boolean inGame = true;

    private Timer timer;

    public SnakeGame() {
        initGame();
    }

    private void initGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.black);
        setFocusable(true);

        addKeyListener(new GameKeyListener());
        initSnake();
        placeApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void initSnake() {
        snakeSize = 3;

        for (int i = 0; i < snakeSize; i++) {
            x[i] = 60 - i * DOT_SIZE;
            y[i] = 60;
        }
    }

    private void placeApple() {
        int r = (int) (Math.random() * RAND_POS);
        appleX = r * DOT_SIZE;

        r = (int) (Math.random() * RAND_POS);
        appleY = r * DOT_SIZE;
    }

    private void move() {
        for (int i = snakeSize; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (isLeft) {
            x[0] -= DOT_SIZE;
        } else if (isRight) {
            x[0] += DOT_SIZE;
        } else if (isUp) {
            y[0] -= DOT_SIZE;
        } else if (isDown) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = snakeSize; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }

        if (y[0] >= BOARD_HEIGHT || y[0] < 0 || x[0] >= BOARD_WIDTH || x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            snakeSize++;
            placeApple();
        }
    }

    private void draw(Graphics g) {
        if (inGame) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, DOT_SIZE, DOT_SIZE);

            for (int i = 0; i < snakeSize; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(Color.white);
                }
                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String message = "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(message, (BOARD_WIDTH - metrics.stringWidth(message)) / 2, BOARD_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !isRight) {
                isLeft = true;
                isUp = false;
                isDown = false;
            } else if (key == KeyEvent.VK_RIGHT && !isLeft) {
                isRight = true;
                isUp = false;
                isDown = false;
            } else if (key == KeyEvent.VK_UP && !isDown) {
                isUp = true;
                isRight = false;
                isLeft = false;
            } else if (key == KeyEvent.VK_DOWN && !isUp) {
                isDown = true;
                isRight = false;
                isLeft = false;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new SnakeGame());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
