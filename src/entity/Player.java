package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;
import main.Sound;

/**
 * Handles the player character, movement, and sword display.
 */
public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    private BufferedImage swordImage;
    private boolean showSword = false;
    private boolean swordWasShowing = false;

    Sound sound = new Sound();

    /**
    * Handles the player character and sword display.
    */
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        spriteWidth = 17 * gp.scale;
        spriteHeight = 25 * gp.scale;
       
        // Increase base health so Jester lasts longer
        maxHealth = 8;
        health = 8;

        // Increase invulnerability window so repeated touch hits land less often
        invulnMs = 1500;

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
    * Shows the sword while the Space Bar is held.
    */
    public void update() {
        showSword = keyH.spacePressed && gp.combat;

        if (showSword && !swordWasShowing) {
            playSFX(6);
            keyH.spacePressed = false;
        }
        swordWasShowing = showSword;

        if (spriteCounter > 5) {
            spriteNum++;
            if (spriteNum == 4) {
                spriteNum = 0;
            }
            spriteCounter = 0;
        } else {
            spriteCounter++;
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

            int minX = spriteWidth / 2;
            int maxX = gp.w - spriteWidth / 2;
            int minY = spriteHeight / 2;
            int maxY = gp.h - spriteHeight / 2;
            if (x < minX) {
                x = minX;
            }
            if (x > maxX) {
                x = maxX;
            }
            if (y < minY) {
                y = minY;
            }
            if (y > maxY) {
                y = maxY;
            } else {
                //comment

            }
        }
    }

    /**
    * Draws the player and the sword (if active).
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

            if (drawDirection == 2) {
                g2.drawImage(swordImage, swordX + swordW, swordY, -swordW, swordH, null);
            } else {
                g2.drawImage(swordImage, swordX, swordY, swordW, swordH, null);
            }
        }

        drawHealthBar(g2);
    }

    public boolean isSlashing() {
        return showSword;
    }

    /**
    * Hitbox for the queen.
    */   

    public Rectangle getSwordHitbox() {
        if (!showSword || swordImage == null) {

            return new Rectangle(0, 0, 0, 0);
        }
        int w = spriteWidth;
        int h = spriteHeight / 2;
        int sx;
        int sy;
        if (drawDirection == 2) {
            sx = x - spriteWidth / 2 - w;
            sy = y - h / 2;
        } else {
            sx = x + spriteWidth / 2;
            sy = y - h / 2;
        }
        return new Rectangle(sx, sy, w, h);
    }

    private void drawHealthBar(Graphics2D g2) {
        int barW = 80;
        int barH = 8;
        int bx = 12;
        int by = 12;
        g2.setColor(new java.awt.Color(60, 60, 60));
        g2.fillRect(bx - 1, by - 1, barW + 2, barH + 2);
        g2.setColor(new java.awt.Color(180, 0, 0));
        int w = (int) (barW * (health / (double) maxHealth));
        g2.fillRect(bx, by, Math.max(0, w), barH);
    }

    /**
     * Plays a sound effect.
     */
    public void playSFX(int i) {
        sound.setFile(i);
        sound.play();
    }

    public void playHurtSFX() {
        playSFX(7);
    }
}
