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
import java.awt.RenderingHints;
import javax.swing.JPanel;

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

    public GamePanel() {
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(new Color(81, 128, 105));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

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

    public void update() {
        if (!timerRunning) {
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
            }
        }

        if (points == 8) {
            playSFX(2);
            points = 0;
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
        }
        if (queen.combat) {
            queen.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

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

        g2.dispose();
    }

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

    private void drawPoints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g2.drawString("Points: " + points, w - 150, 40);
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopSound() {
        sound.stop();
    }

    public void playSFX(int i) {
        sound.setFile(i);
        sound.play();
    }
}
