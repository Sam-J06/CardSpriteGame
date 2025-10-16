package cards;

import java.awt.image.BufferedImage;

/**
 * Class for all cards.
 */
public class Card {
    int suit;   //0 = hearts, 1 = spades.
    int pattern;    //1 - 7 represents the corresponding pip. 8 = Jack, 9 = King, 10 = Queen
    BufferedImage cardImage;
    boolean flipped = false;
    int cardX;
    int cardY;
    int row;
    int column;

    /**
     * Constructs a card.
     */
    Card(int suit, int pattern, BufferedImage cardImage) {

        // this.gp = gp;
        this.suit = suit;
        this.pattern = pattern;
        this.cardImage = cardImage;
        flipped = false;

        System.out.println("New " + suit + " " + pattern + "!");

    }
}
