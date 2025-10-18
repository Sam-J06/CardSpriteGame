package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

/**
 * All player information and methods.
 */
public class King extends Entity {

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
    public King(Player player, GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.player = player;

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
                        "/res/king_sprites/king_" + i + "_" + j + ".png"));
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

        //If the counter is over 5, go to next frame of King animation.
        spriteCounter++;
        if (spriteCounter > 5) {
            spriteNum++;
            if (spriteNum == 4) {
                spriteNum = 0;
            }
            spriteCounter = 0;
        }

        //If any direction key is pressed, go in that direction by speed.
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

            if (!collisionOn) { //checks collision (will we need this?)
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
            //I assume animations will go here.
        }
        
        //experimental enemy follower algorithm.
        if (x > player.x) {
            drawDirection = 2;
        } else {
            drawDirection = 3;
        }
        int sped = 10;
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

        //checks which frame of the animation, and direction for King.
        image = sprite[drawDirection][spriteNum];

        g2.drawImage(image, x - spriteWidth / 2, y - spriteHeight / 2,
            spriteWidth, spriteHeight, null);
    }

    /**
     * Sets king into combt which updates and draws him in GamePanel.
     * Starts playing music.
     */
    public void spawn() {
        combat = true;
        gp.playMusic(3);
    }

    /**
     * Sets king out of combat.
     * Stops music.
     */
    public void despawn() {
        combat = false;
        gp.stopSound();
    }
    
}
