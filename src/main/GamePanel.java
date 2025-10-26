package main;

import cards.MatchCards;
import entity.King;
import entity.Player;
import entity.Queen;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Handles the main game screen, updates, and drawing everything.
 */
public class GamePanel extends JPanel implements Runnable {
    public final int scale = 2;
    public final int w = scale * 349;
    public final int h = scale * 250;

    public final int fps = 30;

    KeyHandler keyH = new KeyHandler();

    public int points = 0;

    public Sound sound = new Sound();

    public Player player = new Player(this, keyH);

    public King king = new King(player, this, keyH);

    public Queen queen = new Queen(player, this, keyH);

    public MatchCards cards = new MatchCards(this, keyH, king, queen);

    public Thread gameThread;

    private int timeLeft = 60;
    private long lastTimerUpdate = System.currentTimeMillis();
    private boolean timerRunning = true;
    private boolean timerWarningPlayed = false;

    private int playerDamage = 1;
    private int enemyTouchDamage = 1;

    public boolean combat;

    // Extra i-frames just for touch damage so the Jester doesn't die too fast from constant overlap
    private long lastContactHitTime = 0;
    private int contactIframesMs = 1600;

    // Game over state and "Try Again" button
    private boolean gameOver = false;
    private Rectangle tryAgainBtn = new Rectangle(0, 0, 0, 0);
    private boolean prevMousePressed = false;

    // Background image
    private BufferedImage backgroundImage;

    /**
     * Sets up the game window and input.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(new Color(81, 128, 105));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(keyH);
        this.setFocusable(true);

        // Load arena background image
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/res/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the main game thread.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Main game loop, runs update and draw.
     */
    public void run() {
        double drawInterval = 1000000000 / fps;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates player, cards, enemies, and timer.
     */
    public void update() {

        combat = king.combat || queen.combat;

        // Handle button click (edge-trigger) when game over
        if (gameOver) {
            boolean justClicked = keyH.mousePressed && !prevMousePressed;
            prevMousePressed = keyH.mousePressed;
            if (justClicked && tryAgainBtn.contains(keyH.mouseX, keyH.mouseY)) {
                resetGame();
            }
            return;
        }

        if (!timerRunning) {
            gameOver = true;
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTimerUpdate >= 1000) {
            timeLeft--;
            lastTimerUpdate = currentTime;

            if (timeLeft == 20 && !timerWarningPlayed) {
                playSFX(4);
                timerWarningPlayed = true;
            }

            if (timeLeft <= 0) {
                timerRunning = false;
                playSFX(5);
                gameOver = true;
                //TODO: stop enemy music
            }
        }

        if (keyH.escapePressed) {
            System.exit(0);
        }
        if (keyH.cPressed && !king.combat) {
            king.spawn();
        } else if (keyH.cPressed) {
            king.despawn();
        }
        if (keyH.mPressed && !queen.combat) {
            queen.spawn();
        } else if (keyH.mPressed) {
            queen.despawn();
        }

        cards.update(player);
        player.update();

        if (king.combat) {
            king.update();
            handleSlashDamage(king);
            handleEnemyTouchDamage(king);
            if (!king.isAlive()) {
                king.despawn();
            }
        }
        if (queen.combat) {
            queen.update();
            handleSlashDamage(queen);
            handleEnemyTouchDamage(queen);
            if (!queen.isAlive()) {
                queen.despawn();
            }
        }

        // Check player death → game over
        if (!player.isAlive()) {
            timerRunning = false;
            gameOver = true;
        }

        prevMousePressed = keyH.mousePressed;
    }

    /**
     * Handles sword damage to enemies when slashing.
     */
    private void handleSlashDamage(entity.Entity enemy) {
        if (!player.isSlashing()) {
            return;
        }
        Rectangle sBox = player.getSwordHitbox();
        if (sBox.width <= 0 || sBox.height <= 0) {
            return;
        }    
        if (sBox.intersects(enemy.getBounds())) {
            enemy.takeDamage(playerDamage);
        }
    }

    /**
     * Handles touch damage when player collides with an enemy.
     */
    private void handleEnemyTouchDamage(entity.Entity enemy) {
        Rectangle pBox = player.getBounds();
        Rectangle eBox = enemy.getBounds();

        if (!pBox.intersects(eBox)) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastContactHitTime < contactIframesMs) {
            return;
        }

        long prevHit = player.lastHitTime;
        player.takeDamage(enemyTouchDamage);
        boolean actuallyHit = (player.lastHitTime != prevHit);

        if (actuallyHit) {
            lastContactHitTime = now;
            player.playHurtSFX();

            int dx = player.x - enemy.x;
            int dy = player.y - enemy.y;
            int dist = Math.max(1, (int) Math.hypot(dx, dy));
            int kb = Math.max(2, scale * 2);
            int kx = Math.round(kb * dx / (float) dist);
            int ky = Math.round(kb * dy / (float) dist);
            player.x += kx;
            player.y += ky;

            int minX = player.spriteWidth / 2;
            int maxX = w - player.spriteWidth / 2;
            int minY = player.spriteHeight / 2;
            int maxY = h - player.spriteHeight / 2;
            if (player.x < minX) {
                player.x = minX;
            }
            if (player.x > maxX) {
                player.x = maxX;
            }
            if (player.y < minY) {
                player.y = minY;
            }
            if (player.y > maxY) {
                player.y = maxY;
            }
        }
    }

    /**
     * Draws everything on screen.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, w, h, null);
        }

        cards.draw(g2);
        player.draw(g2);

        if (king.combat) {
            king.draw(g2);
        }
        if (queen.combat) {
            queen.draw(g2);
        }

        drawTimer(g2);
        drawPoints(g2);
        drawPlayerHPText(g2);

        if (gameOver) {
            drawGameOverOverlay(g2);
        }

        g2.dispose();
    }

    /**
     * Draws the timer.
     */
    private void drawTimer(Graphics2D g2) {
        int radius = 30;
        int centerX = 80;
        int centerY = 65;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color woodBrown = new Color(102, 51, 0);
        Color woodHighlight = new Color(153, 102, 51);
        g2.setColor(woodBrown);
        g2.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        g2.setColor(woodHighlight);
        g2.setStroke(new BasicStroke(5));
        g2.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        g2.setColor(new Color(204, 153, 102));
        g2.fillOval(centerX - radius + 5, centerY - radius + 5, (radius - 5) * 2, (radius - 5) * 2);

        double angle = Math.toRadians((360.0 / 60) * (60 - timeLeft) - 90);
        int handLength = radius - 15;
        int handX = centerX + (int) (Math.cos(angle) * handLength);
        int handY = centerY + (int) (Math.sin(angle) * handLength);
        g2.setColor(new Color(30, 30, 30));
        g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(centerX, centerY, handX, handY);

        g2.setColor(new Color(80, 80, 80));
        g2.fillOval(centerX - 5, centerY - 5, 10, 10);

        g2.setColor(new Color(80, 50, 20));
        int spikeCount = 24;
        int outerRadius = radius + 3;
        int innerRadius = radius - 1;
        for (int i = 0; i < spikeCount; i++) {
            double spikeAngle = Math.toRadians(i * (360.0 / spikeCount));
            int x1 = centerX + (int) (Math.cos(spikeAngle) * innerRadius);
            int y1 = centerY + (int) (Math.sin(spikeAngle) * innerRadius);
            int x2 = centerX + (int) (Math.cos(spikeAngle) * outerRadius);
            int y2 = centerY + (int) (Math.sin(spikeAngle) * outerRadius);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Times New Roman", Font.BOLD, 15));
        if (timeLeft > 0) {
            g2.drawString(timeLeft + "s", centerX - 10, centerY + radius + 15);
        } else {
            g2.drawString("Time's up!", centerX - 30, centerY + radius + 15);
        }
    }

    /**
     * Draws the points counter.
     */
    private void drawPoints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g2.drawString("Points: " + points, w - 150, 40);
    }

    private void drawPlayerHPText(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        g2.drawString("HP: " + player.health + "/" + player.maxHealth, 12, 35);
    }

    /**
     * Draws the Game Over overlay and "Try Again" button.
     */
    private void drawGameOverOverlay(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dims background
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, w, h);

        // Button 
        int btnW = 180;
        int btnH = 60;
        int cx = w / 2;
        int cy = h / 2 + 10;
        int bx = cx - btnW / 2;
        int by = cy - btnH / 2;
        tryAgainBtn.setBounds(bx, by, btnW, btnH);

        // Button 
        Color woodBrown = new Color(102, 51, 0);
        Color woodHighlight = new Color(153, 102, 51);
        Color woodInner = new Color(204, 153, 102);

        g2.setColor(woodBrown);
        g2.fillRoundRect(bx - 4, by - 4, btnW + 8, btnH + 8, 14, 14);
        g2.setColor(woodHighlight);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(bx - 4, by - 4, btnW + 8, btnH + 8, 14, 14);
        g2.setColor(woodInner);
        g2.fillRoundRect(bx, by, btnW, btnH, 10, 10);

        g2.setColor(new Color(80, 50, 20));
        int spikeCount = 16;
        int pad = 8;
        for (int i = 0; i < spikeCount; i++) {
            double t = (2 * Math.PI / spikeCount) * i;
            int rx = cx + (int) (Math.cos(t) * (btnW / 2 + pad));
            int ry = cy + (int) (Math.sin(t) * (btnH / 2 + pad));
            int ix = cx + (int) (Math.cos(t) * (btnW / 2));
            int iy = cy + (int) (Math.sin(t) * (btnH / 2));
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(ix, iy, rx, ry);
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Times New Roman", Font.BOLD, 22));
        String label = "Try Again";
        int sw = g2.getFontMetrics().stringWidth(label);
        int sh = g2.getFontMetrics().getAscent();
        g2.drawString(label, cx - sw / 2, cy + sh / 3);

        g2.setFont(new Font("Times New Roman", Font.BOLD, 28));
        g2.setColor(Color.WHITE);
        String msg = (timeLeft <= 0) ? "Time's Up!" : "";
        int mw = g2.getFontMetrics().stringWidth(msg);
        g2.drawString(msg, cx - mw / 2, by - 18);
    }

    /**
     * Resets everything for a new game.
     */
    private void resetGame() {
        stopSound();
        timeLeft = 60;
        lastTimerUpdate = System.currentTimeMillis();
        timerRunning = true;
        timerWarningPlayed = false;
        points = 0;
        player.health = player.maxHealth;
        player.setDefaultValues();
        player.lastHitTime = 0;
        if (king.combat) {
            king.despawn();
        }
        if (queen.combat) {
            queen.despawn();
        }
        cards.setupCards();
        lastContactHitTime = 0;
        gameOver = false;
    }

    /**
     * Plays background music.
     */
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    /**
     * Stops current music.
     */
    public void stopSound() {
        sound.stop();
    }

    /**
     * Plays a sound effect.
     */
    public void playSFX(int i) {
        sound.setFile(i);
        sound.play();
    }
}
