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
    public int spriteHeight;
    public int spriteWidth;
    Player player;

    /**
     * Constructor.
     */
    public Queen(Player player, GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.player = player;

        //update for each sprite

        //jester values
        // spriteWidth = 17 * gp.scale;
        // spriteHeight = 25 * gp.scale;

        spriteWidth = 27 * gp.scale;
        spriteHeight = 55 * gp.scale;

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
                        "/res/Queen_sprites/Queen_" + i + "_" + j + ".png"));
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
        if (keyH.uPressed || keyH.dPressed || keyH.lPressed || keyH.rPressed) {

            if (keyH.uPressed) {
                moveDirection = 0;
            } else if (keyH.dPressed) {
                moveDirection = 1;
            } else if (keyH.lPressed) {
                moveDirection = 2;
            } else if (keyH.rPressed) {
                moveDirection = 3;
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
                    default:
                        break;
                }
            }

            //add animation here.
            
        }
        
        //experimental enemy follower algorithm.

        if (x > player.x) {
            drawDirection = 2;
        } else {
            drawDirection = 3;
        }
        int sped = 40;
        if (Math.abs(Math.abs(x - player.x) - Math.abs(y - player.y)) > 100) {
            // sped = 10;
        }
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

        // image = sprite[moveDirection][spriteNum];
        image = sprite[drawDirection][spriteNum];

        g2.drawImage(image, x - spriteWidth / 2, y - spriteHeight / 2,
            spriteWidth, spriteHeight, null);
    }
    
}
