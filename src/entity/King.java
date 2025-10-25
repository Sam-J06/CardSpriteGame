package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

/**
 * King enemy.
 */
public class King extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public boolean combat = false;

    
    Player player;
    /**
    * King enemy.
    */

    public King(Player player, GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.player = player;

        spriteWidth = 27 * gp.scale;
        spriteHeight = 55 * gp.scale;

        maxHealth = 6;
        health = 6;

        setDefaultValues();
        getPlayerImage();
    }
    /**
    * method sets some values for the King enemy.
    */  

    public void setDefaultValues() {
        x = gp.w - 100;
        y = 100;
        speed = gp.w / 100;
        moveDirection = 0;
        drawDirection = 2;
    }

    /** Load sprites. */
    public void getPlayerImage() {
        try {
            for (int i = 2; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    sprite[i][j] = ImageIO.read(getClass().getResourceAsStream(
                        "/res/king_sprites/king_" + i + "_" + j + ".png"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Track the player. */
    public void update() {

        spriteCounter++;
        if (spriteCounter > 5) {
            spriteNum = (spriteNum + 1) % 4;
            spriteCounter = 0;
        }

        drawDirection = (x > player.x) ? 2 : 3;

        int sped = 10; // lower is faster due to division
        if (Math.abs(Math.abs(x - player.x) - Math.abs(y - player.y)) < 10) {
            x += (player.x - x) / sped;
            y += (player.y - y) / sped;
        } else if (Math.abs(x - player.x) > Math.abs(y - player.y)) {
            x += (player.x - x) / sped;
        } else {
            y += (player.y - y) / sped;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = sprite[drawDirection][spriteNum];
        g2.drawImage(image, x - spriteWidth / 2, y - spriteHeight / 2,
            spriteWidth, spriteHeight, null);
        drawHealthBar(g2);
    }

    /** Enter combat and reset HP/i-frames; start music. */
    public void spawn() {
        combat = true;
        gp.playMusic(3);
        health = maxHealth;
        lastHitTime = 0;
        setDefaultValues();
    }

    /** Leave combat and stop music. */
    public void despawn() {
        combat = false;
        gp.stopSound();
    }

    private void drawHealthBar(Graphics2D g2) {
        int barW = spriteWidth;
        int barH = 4;
        int bx = x - barW / 2;
        int by = y - spriteHeight / 2 - 8;
        g2.setColor(new java.awt.Color(30, 30, 30));
        g2.fillRect(bx - 1, by - 1, barW + 2, barH + 2);
        g2.setColor(new java.awt.Color(200, 30, 30));
        int w = (int) (barW * (health / (double) maxHealth));
        g2.fillRect(bx, by, Math.max(0, w), barH);
    }
}
