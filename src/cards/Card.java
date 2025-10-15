package cards;

import entity.Entity;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

/**
 * Class for all cards.
 */
public class Card {
    public int pattern;
    GamePanel gp;
    KeyHandler keyH;

    public BufferedImage[] cardImage = new BufferedImage[11];

    public boolean flipped = false;

    Entity entity;


    public int cardWidth;
    public int cardHeight;

    public int x;
    public int y;
    public int sign;


    

    /**
     * Constructor for card.
     */
    public Card(int sign, int x, int y, GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;
        this.x = x;
        this.y = y;
        this.sign = sign;

        setValue();
        getCardImage();
    }

    /**
     * Places card on screen.
     */
    public void setValue() {
        cardWidth = 37 * gp.scale;
        cardHeight = 52 * gp.scale;

    }

    /**
     * Reads resource file for card sprite.
     */
    public void getCardImage() {
        try {
        
            for (int i = 0; i < 11; i++) {
                cardImage[i] = ImageIO.read(getClass().getResourceAsStream("/res/cards/Card_"
                + i + ".png"));
            }

            // cardIamge[11] = null;
            
            /*
             * 0 back
             * 1 ace
             * 2 - 7 is 2 - 7
             * 8 jack
             * 9 king
             * 10 queen
             * 
             */
            


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * updates card.
     */
    public void update(int playerx, int playery) {
        if (playerx > x && playerx < x + cardWidth && playery > y
                && playery < y + cardHeight) {
            if (keyH.space && !flipped) {
                flipped = true;
            }
        }

        
    }

    /**
     * draw.
     */
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        
        if (flipped) {
            image = cardImage[sign];
        } else {
            image = cardImage[0];
        }
        // System.out.println(x + " " + y);

        g2.drawImage(image, x, y, cardWidth, cardHeight, null);
    }
}
