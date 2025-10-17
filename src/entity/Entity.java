package entity;

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

    //The array sprite[i][j] are all the sprites where
    //i is direction (0 = up, 1 = down, 2 = left, 3 = right)
    //j is different sprites

    public int spriteCounter = 0;
    public int spriteNum = 0;   //Denotes which particular sprite (/frame)

    public boolean collisionOn = false; //To add


}
