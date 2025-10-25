package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Super class for all entities, including the player.
 */
public class Entity {

    public int x;
    public int y;
    public int speed;

    public BufferedImage[][] sprite = new BufferedImage[4][4];
    public int moveDirection;
    public int drawDirection;

    // sprite[i][j]: i=dir (0=up,1=down,2=left,3=right); j=frame 0..3
    public int spriteCounter = 0;
    public int spriteNum = 0;

    public boolean collisionOn = false;

    public int maxHealth = 5;
    public int health = 5;
    public int spriteWidth = 0;
    public int spriteHeight = 0;
    public long lastHitTime = 0;
    public int invulnMs = 500;

    public boolean isAlive() {
        return health > 0;
    }

    /** Take damage with invulnerability frames. */
    public void takeDamage(int dmg) {
        long now = System.currentTimeMillis();
        if (now - lastHitTime < invulnMs) {
            return;
        }
        health = Math.max(0, health - dmg);
        lastHitTime = now;
    }

    /** Bounds used for combat collisions. */
    public Rectangle getBounds() {
        int w = Math.max(1, spriteWidth);
        int h = Math.max(1, spriteHeight);
        return new Rectangle(x - w / 2, y - h / 2, w, h);
    }
}
