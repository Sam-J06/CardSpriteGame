package cards;

import entity.Player;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

/**
 * This class creates an array list of Cards which is used in the GamePanel.
 */
public class MatchCards {

    ArrayList<Card> cardset;
    int numberOfPairs = 8;
    int numberOfCards = numberOfPairs * 2;
    GamePanel gp;
    KeyHandler keyH;
    BufferedImage backOfCard;
    int cardWidth;
    int cardHeight;
    int rowsOfCards = 4 - 1;
    int columnsOfCards = 5 - 1;
    int card1Select = -1;
    int card2Select = -1;
    
    
    /**
     * This method is run when the an instnce of MatchCards is created.
     * This will be done in GamePanel.java.
     */
    public MatchCards(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        cardWidth = 37 * gp.scale;
        cardHeight = 52 * gp.scale;

        setupCards();
        // shuffleCards(); CARD SHUFFLE AT START DISABLED. TYPE 'C' FOR SHUFFLE

        
    }

    /**
     * Assigns values to all cards.
     */
    public void setupCards() {

        cardset = new ArrayList<>();    //Creates an arraylist of all 20 cards.

        try {

            int suit = 0;
            int pattern = 1;
            for (int i = 0; i < numberOfCards; i++) {
                if (pattern > numberOfPairs) {
                    suit = 1;
                    pattern = 1;
                }
                BufferedImage image = null;
                image = ImageIO.read(getClass().getResourceAsStream("/res/card_sprites/Card_"
                    + suit + "_" + (i % numberOfPairs + 1) + ".png"));
                Card card = new Card(suit, (i % numberOfPairs + 1), image);
                cardset.add(card);
                pattern++;
            }
        
            backOfCard = ImageIO.read(getClass().getResourceAsStream(
                    "/res/card_sprites/Card_0_0.png"));

            
        } catch (Exception e) {
            // TODO: handle exception
        }

        shuffleCards();

    }


    /**
     * Updates card info.
     */
    public void update(Player player) {

        // if (keyH.spacePressed) {
        //     for (int i = 0; i < numberOfCards; i++) {
        //         cardset.get(i).flipped = true;
        //         System.out.println("flipped card "
        //             + cardset.get(i).suit + " " + cardset.get(i).pattern + "!");
        //     }
        // }


        // if (keyH.spacePressed) {
        //     for (int i = 0; i < numberOfCards; i++) {
        //         if (player.x > cardset.get(i).cardX && player.x < cardset.get(i).cardX + cardWidth && player.y > cardset.get(i).cardY && player.y < cardset.get(i).cardY + cardHeight) {
        //             cardset.get(i).flipped = true;
        //         }
        //     }
        // }

        


        if (keyH.spacePressed && !(gp.kingCombat || gp.queenCombat)) {
            for (int i = 0; i < numberOfCards; i++) {

                if (!cardset.get(i).flipped && player.x > cardset.get(i).cardX
                    && player.x < cardset.get(i).cardX + cardWidth
                    && player.y > cardset.get(i).cardY
                    && player.y < cardset.get(i).cardY + cardHeight) { //if card is face down
                
                    if (card1Select == -1) {    //and 1st card not sected

                        card1Select = i;
                        cardset.get(i).flipped = true;  //select first card and flip
                        System.out.println("card 1 selected");

                    } else if (card2Select == -1) { //or 2nd card not selected

                        card2Select = i;
                        cardset.get(i).flipped = true;  //select 2nd card and flip
                        System.out.println("card 2 selected");


                        if (cardset.get(card1Select).pattern != cardset.get(card2Select).pattern) {
                            System.out.println("wrong");

                            cardset.get(card1Select).flipped = false;
                            cardset.get(card2Select).flipped = false;

                            card1Select = -1;
                            card2Select = -1;

                            gp.kingCombat = true;
                            gp.queenCombat = true;

                            System.out.println("card selection reset");
                        } else {
                            
                            System.out.println("correct");

                            card1Select = -1;
                            card2Select = -1;

                            System.out.println("card selection reset");
                        }

                    }

                }

            }
        } else if (keyH.spacePressed && (gp.kingCombat || gp.queenCombat)) {
            System.out.println("attack!");

        }


        
    }

    /**
     * Shuffles cards.
     */
    public void shuffleCards() {
        for (int i = 0; i < numberOfCards; i++) {
            int j = (int) (Math.random() * cardset.size()); //gets random index from 0 - 19
            Card temp = cardset.get(i);
            cardset.set(i, cardset.get(j));
            cardset.set(j, temp);
        }
        getXYColumnRow();
        System.out.println("Cards shuffled");
        

    }

    /**
     * Itertes and draws all cards in the array list.
     */
    public void draw(Graphics2D g2) {

        for (int i = 0; i < numberOfCards; i++) {
            BufferedImage image = null;
            if (cardset.get(i).flipped) {
                image = cardset.get(i).cardImage;
            } else {
                image = backOfCard;
                // image = cardset.get(i).cardImage;
            }
            g2.drawImage(image, cardset.get(i).cardX, cardset.get(i).cardY,
                cardWidth, cardHeight, null);
        }

    }

    /**
     * Updates the row, column, x and y information of each card in the arraylist.
     */
    public void getXYColumnRow() {
        int row = 0;
        int column = 0;
        for (int i = 0; i < numberOfCards; i++) {
            if (column > 3) {
                row++;
                column = 0;
            }
            cardset.get(i).row = row;
            cardset.get(i).column = column;
            cardset.get(i).cardX = gp.scale * (80 + 39 / 2 + 39 * column);
            cardset.get(i).cardY = gp.scale * (16 + 56 * row);
            System.out.println("set " + cardset.get(i).suit + " "
                + cardset.get(i).pattern + " to column " + column + " row " + row);
            column++;
        }
    }

}


