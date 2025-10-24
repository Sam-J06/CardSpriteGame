package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

/**
 * Handles the player character, movement, and sword display.
 */
public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public int x;
    public int y;

    public int spriteHeight;
    public int spriteWidth;

    private BufferedImage swordImage;
    private boolean showSword = false;
    private boolean swordWasShowing = false;

    /**
    * Handles the player character and sword display.
    */
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        spriteWidth = 17 * gp.scale;
        spriteHeight = 25 * gp.scale;

        setDefaultValues();
        getPlayerImage();
        getSwordImage();
    }

    /**
    * Handles the player character movement.
    */
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = gp.w / 100;
        drawDirection = 2;
        moveDirection = 0;
    }

    /**
    * Handles the display of the player character.
    */
    public void getPlayerImage() {
        try {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    sprite[i][j] = ImageIO.read(getClass().getResourceAsStream(
                        "/res/jester.png"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Loads the sword image.
    */
    public void getSwordImage() {
        try {
            swordImage = ImageIO.read(getClass().getResourceAsStream("/res/sword.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Shows the sword while the LEFT MOUSE BUTTON is held.
    */
    public void update() {
        
        showSword = keyH.mousePressed;

        if (showSword && !swordWasShowing) {
            gp.playSFX(6);
        }
        swordWasShowing = showSword;

        if (spriteCounter > 5) {
            spriteNum++;
            if (spriteNum == 4) {
                spriteNum = 0;
            }
            spriteCounter = 0;
        }

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if (keyH.upPressed) {
                moveDirection = 0;
            } else if (keyH.downPressed) {
                moveDirection = 1;
            } else if (keyH.leftPressed) { 
                moveDirection = 2; 
                drawDirection = 2; 
            } else if (keyH.rightPressed) {
                moveDirection = 3;
                drawDirection = 3; 
            }

            if (!collisionOn) {
                switch (moveDirection) {
                    case 0: y -= speed; 
                    break;
                    case 1: y += speed; 
                    break;
                    case 2: x -= speed; 
                    break;
                    case 3: x += speed; 
                    break;
                }
            }

            x = (x + gp.w) % gp.w;
            y = (y + gp.h) % gp.h;
        }
    }

    /**
    * Draws the player and the sword.
    */
    public void draw(Graphics2D g2) {
        BufferedImage image = sprite[moveDirection][spriteNum];
        g2.drawImage(image, x - spriteWidth / 2, y - spriteHeight / 2,
            spriteWidth, spriteHeight, null);

        if (showSword && swordImage != null) {
            int swordW = spriteWidth * 2;
            int swordH = swordImage.getHeight() * swordW / swordImage.getWidth() - 150;
            int swordX = x - swordW / 2;
            int swordY = y - spriteHeight / 200 - swordH / 2;
            g2.drawImage(swordImage, swordX, swordY, swordW, swordH, null);
        }
    }
}
