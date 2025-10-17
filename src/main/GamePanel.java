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


    public boolean kingCombat;
    public boolean queenCombat;

    public int points = 0;

   
    //Initiation of Classes.
    public Sound sound = new Sound();

    public MatchCards cards = new MatchCards(this, keyH);

    public Player player = new Player(this, keyH);

    public King king = new King(player, this, keyH);

    public Queen queen = new Queen(player, this, keyH);


    public Thread gameThread;

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
     * starts gamethread. Please read more on this.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * The GameLoop is located here.
     */
    public void run() {
        double drawInterval = 1000000000 / fps; //1 second in nano / fps.
        double nextDrawTime = System.nanoTime() + drawInterval;
        

        while (gameThread != null) {
            
            update();
            repaint(); //included method. Please read.

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

        //Plays sound effect if game is completed.
        if (points == 8) {
            playSFX(2);
            points = 0;
        }

        //'esc' exits. 'm' summons queen. 'c' summons king.
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
        
        //Updating of the initiated classes.
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

    /**
     * Plays looped music.
     */
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();

    }
    
    /**
     * Stops the current sound that plays.
     */
    public void stopSound() {
        sound.stop();

    }

    /**
     * Plays unlooped sound effects.
     */
    public void playSFX(int i) {
        sound.setFile(i);
        sound.play();
    }
    
}
