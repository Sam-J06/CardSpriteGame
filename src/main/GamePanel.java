package main;

import cards.MatchCards;
import entity.King;
import entity.Player;
import entity.Queen;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * class for what goes into opened frame.
 */
public class GamePanel extends JPanel implements Runnable {
    public final int scale = 2;
    public final int w = scale * 349;
    public final int h = scale * 250;

    public final int fps = 30;

    KeyHandler keyH = new KeyHandler();

    public Thread gameThread;

    public boolean kingCombat;
    public boolean queenCombat;

   

    public MatchCards cards = new MatchCards(this, keyH);

    public Player player = new Player(this, keyH);

    public King king = new King(player, this, keyH);

    public Queen queen = new Queen(player, this, keyH);

    /**
     * gamepanel constructor.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(new Color(81, 128, 105));
        this.setDoubleBuffered(true);
        
        this.addKeyListener(keyH);

        this.setFocusable(true);


    }

    /**
     * starts gamethread.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000 / fps; //1 second in nano / fps.
        double nextDrawTime = System.nanoTime() + drawInterval;
        

        while (gameThread != null) {
            
            update();
            repaint();

            // frame++;
            // if (frame == 30) {
            //     System.out.println("30frames passed");
            //     frame = 0;
            // }

            if (keyH.escapePressed) {
                System.exit(0);
            }
            if (keyH.cPressed && !kingCombat) {
                kingCombat = true;
            } else if (keyH.cPressed) {
                kingCombat = false;
            }
            if (keyH.mPressed && !queenCombat) {
                queenCombat = true;
            } else if (keyH.mPressed) {
                queenCombat = false;
            }


            try {
                
                double remainingTime = nextDrawTime -  System.nanoTime();
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
     * Updates other elements such as the player, enemies and cards.
     */
    public void update() {
        
        cards.update(player);
        player.update();
        if (kingCombat) {
            king.update();
        }
        if (queenCombat) {
            queen.update();
        }


    }

    /**
     * A JPanel method that draws onto the panel.
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //TODO: add entity draw methods.

        cards.draw(g2); 

        player.draw(g2);

        if (kingCombat) {
            king.draw(g2);
        }
        if (queenCombat) {
            queen.draw(g2);
        }

        g2.dispose();
    }
    
}
