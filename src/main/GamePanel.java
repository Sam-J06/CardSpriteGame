package main;

import cards.Card; //TODO: change
import entity.Player;
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
    public final int howmany = 10;

    public final int fps = 30;

    KeyHandler keyH = new KeyHandler();

    public Thread gameThread;

    public Player player = new Player(this, keyH);

    //TODO: change.
    public Card[] allcards = new Card[howmany]; 
    {
        int row = 0;
        int column = 0;

        for (int i = 0; i < howmany; i++) {
            allcards[i] = new Card(i + 1, scale * (80 + 39 * column),
                scale * (16 + 56 + 56 * row), this, keyH);
            column++;
            if (column == 5) {
                column = 0;
                row++;
            }
        }
    }
    //what even is this??

    // public int frame = 0;

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

            if (keyH.escape) {
                System.exit(0);
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

        player.update();

        //iterates through all cards to update. TODO: change.
        for (int i = 0; i < howmany; i++) {
            allcards[i].update(player.x, player.y);
        }

    }

    /**
     * A JPanel method that draws onto the panel.
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //TODO: add entity draw methods.

        //iterates through all cards to draw. TODO: change.
        for (int i = 0; i < howmany; i++) {
            allcards[i].draw(g2);
        }
        
        player.draw(g2);

        g2.dispose();
    }
    
}
