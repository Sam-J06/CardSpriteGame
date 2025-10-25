package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

/**
 * All player information and methods.
 */
public class Queen extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public int x;
    public int y;

    //the dimension of the sprites.
    public int spriteHeight;
    public int spriteWidth;

    public boolean combat = false;
    
    //player imported in order to check its x, y.
    Player player;

    /**
     * Constructor.
     */
    public Queen(Player player, GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.player = player;

        spriteWidth = 27 * gp.scale;
        spriteHeight = 55 * gp.scale;
        

        maxHealth = 4;
        health = 4;

        setDefaultValues();
        getPlayerImage();
    }

    /**
     * sets default values.
     */
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = gp.w / 100;
        moveDirection = 0;
        drawDirection = 2;
    }

    /**
     * imports sprites from resource file.
     */
    public void getPlayerImage() {
        try {
            for (int i = 2; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    sprite[i][j] = ImageIO.read(getClass().getResourceAsStream(
                        "/res/queen_sprites/queen_" + i + "_" + j + ".png"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates direction, speed, and player sprite. Gets called in GamePanel class.
     */
    public void update() {

        spriteCounter++;
        if (spriteCounter > 5) {
            spriteNum++;
            if (spriteNum == 4) {
                spriteNum = 0;
            }
            spriteCounter = 0;
        }

        if (x > player.x) {
            drawDirection = 2;
        } else {
            drawDirection = 3;
        }
        int sped = 40;
        if (Math.abs(Math.abs(x - player.x) - Math.abs(y - player.y)) < 10) {
            x += (player.x - x) / sped;
            y += (player.y - y) / sped;
        } else if (Math.abs(x - player.x) > Math.abs(y - player.y)) {
            x += (player.x - x) / sped;
        } else {
            y += (player.y - y) / sped;
        }
    }

    /**
     * Draws elements.
     */
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        image = sprite[drawDirection][spriteNum];
        g2.drawImage(image, x - spriteWidth / 2, y - spriteHeight / 2,
            spriteWidth, spriteHeight, null);

        drawHealthBar(g2);
    }

    /**
     * Sets queen into combt which updates and draws her in GamePanel.
     * Starts playing music.
     */
    public void spawn() {
        combat = true;
        gp.playMusic(3);
        health = maxHealth;
        lastHitTime = 0;
    }

    /**
     * Sets queen out of combat.
     * Stops music.
     */
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
