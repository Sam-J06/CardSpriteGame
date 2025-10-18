package cards;

import entity.King;
import entity.Player;
import entity.Queen;
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
    King king;
    Queen queen;
    
    
    /**
     * This method is run when the an instnce of MatchCards is created.
     * This will be done in GamePanel.java.
     */
    public MatchCards(GamePanel gp, KeyHandler keyH, King king, Queen queen) {
        this.gp = gp;
        this.keyH = keyH;
        this.king = king;
        this.queen = queen;

        cardWidth = 37 * gp.scale;
        cardHeight = 52 * gp.scale;

        setupCards();
    }

    /**
     * Assigns assigns a suit, pattern and image to all cards.
     * Creates an arraylist cardset of all cards.
     * shuffles cards.
     */
    public void setupCards() {

        cardset = new ArrayList<>();    //Creates an arraylist of all 20 cards.

        try {

            int suit = 0;
            int pattern = 1;
            //iterates through all cards to assign suit, pattern and image.
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
        
            //assigns an image to the back of the card.
            backOfCard = ImageIO.read(getClass().getResourceAsStream(
                    "/res/card_sprites/Card_0_0.png"));

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        shuffleCards();

    }


    /**
     * Updates card info.
     */
    public void update(Player player) {

        if (keyH.spacePressed && !(king.combat || queen.combat)) {
            for (int i = 0; i < numberOfCards; i++) {

                if (!cardset.get(i).flipped && player.x > cardset.get(i).cardX
                    && player.x < cardset.get(i).cardX + cardWidth
                    && player.y > cardset.get(i).cardY
                    && player.y < cardset.get(i).cardY + cardHeight) { //if card is face down
                
                    if (card1Select == -1) {  //and 1st card not sected

                        card1Select = i;
                        cardset.get(i).flipped = true;  //select first card and flip
                        System.out.println("card 1 selected");
                        gp.playSFX(0);

                    } else if (card2Select == -1) { //or 2nd card not selected

                        card2Select = i;
                        cardset.get(i).flipped = true;  //select 2nd card and flip
                        System.out.println("card 2 selected");
                        gp.playSFX(0);

                        //compares pattern of selected cards
                        if (cardset.get(card1Select).pattern != cardset.get(card2Select).pattern) {
                            //if non matching cards are selected.
                            System.out.println("wrong");

                            cardset.get(card1Select).flipped = false;
                            cardset.get(card2Select).flipped = false;
                            gp.playSFX(1);

                            card1Select = -1;
                            card2Select = -1;

                            System.out.println("card selection reset");
                        } else if (cardset.get(card1Select).pattern == 7) {
                            king.spawn();

                            gp.points++;
                            System.out.println("correct. Points: " + gp.points);
                            
                            card1Select = -1;
                            card2Select = -1;

                            System.out.println("card selection reset");
                        } else if (cardset.get(card1Select).pattern == 8) {
                            // gp.queenCombat = true;
                            queen.spawn();

                            gp.points++;
                            System.out.println("correct. Points: " + gp.points);
                            
                            card1Select = -1;
                            card2Select = -1;

                            System.out.println("card selection reset");
                        } else {
                            //if matching cards are selected.
                            gp.points++;
                            System.out.println("correct. Points: " + gp.points);
                            
                            card1Select = -1;
                            card2Select = -1;

                            System.out.println("card selection reset");
                        }
                    }
                }
            }

        } 
    }

    /**
     * Shuffles cards.
     */
    public void shuffleCards() {
        for (int i = 0; i < numberOfCards; i++) {
            int j = (int) (Math.random() * cardset.size()); //gets random index from 0 - 17
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

        //iterates through all cards
        for (int i = 0; i < numberOfCards; i++) {

            BufferedImage image = null;

            //checks if card is flipped or not.
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
     * Columns are determined by if loop.
     * Rows are determined by the number of cards.
     */
    public void getXYColumnRow() {

        int row = 0;
        int column = 0;
        for (int i = 0; i < numberOfCards; i++) {
            if (column > 3) {   //goes to new row after 4 columns.
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


