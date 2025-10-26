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
 * Handles the card-matching.
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

    boolean waitingToFlipBack = false;
    long flipBackTime = 0;
    long flipDelay = 1000;

    // New field: controls whether the preview period is active
    private boolean showingPreview = true;
    private long previewEndTime = 0;

    /**
     * Sets up the card game with sizes, input, and enemies to trigger on matches.
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
     * Loads all card faces and the back image, then shuffles.
     */
    public void setupCards() {

        cardset = new ArrayList<>();

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
            e.printStackTrace();
        }

        shuffleCards();

        
        for (Card c : cardset) {
            c.flipped = true;
        }
        showingPreview = true;
        previewEndTime = System.currentTimeMillis() + 1500; 
    }

    /**
     * Handles input and match logic.
     */
    public void update(Player player) {

        
        if (showingPreview) {
            if (System.currentTimeMillis() >= previewEndTime) {
                for (Card c : cardset) {
                    c.flipped = false;
                }
                showingPreview = false;
            } else {
                return; // Skip input until preview ends
            }
        }

        if (waitingToFlipBack) {
            if (System.currentTimeMillis() >= flipBackTime) {
                System.out.println("wrong");
                cardset.get(card1Select).flipped = false;
                cardset.get(card2Select).flipped = false;
                gp.playSFX(1);
                card1Select = -1;
                card2Select = -1;
                waitingToFlipBack = false;
                System.out.println("card selection reset");
            }
            return;
        }

        if (keyH.spacePressed && !(king.combat || queen.combat)) {
            for (int i = 0; i < numberOfCards; i++) {

                if (!cardset.get(i).flipped && player.x > cardset.get(i).cardX
                        && player.x < cardset.get(i).cardX + cardWidth
                        && player.y > cardset.get(i).cardY
                        && player.y < cardset.get(i).cardY + cardHeight) {

                    if (card1Select == -1) {
                        card1Select = i;
                        cardset.get(i).flipped = true;
                        System.out.println("card 1 selected");
                        gp.playSFX(0);

                    } else if (card2Select == -1) {
                        card2Select = i;
                        cardset.get(i).flipped = true;
                        System.out.println("card 2 selected");
                        gp.playSFX(0);

                        if (cardset.get(card1Select).pattern != cardset.get(card2Select).pattern) {
                            waitingToFlipBack = true;
                            flipBackTime = System.currentTimeMillis() + flipDelay;

                        } else if (cardset.get(card1Select).pattern == 7) {
                            king.spawn();
                            gp.points++;
                            System.out.println("correct. Points: " + gp.points);
                            card1Select = -1;
                            card2Select = -1;
                            System.out.println("card selection reset");

                        } else if (cardset.get(card1Select).pattern == 8) {
                            queen.spawn();
                            gp.points++;
                            System.out.println("correct. Points: " + gp.points);
                            card1Select = -1;
                            card2Select = -1;
                            System.out.println("card selection reset");

                        } else {
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
     * Randomizes the card order and lays them out on the grid.
     */
    public void shuffleCards() {
        for (int i = 0; i < numberOfCards; i++) {
            int j = (int) (Math.random() * cardset.size());
            Card temp = cardset.get(i);
            cardset.set(i, cardset.get(j));
            cardset.set(j, temp);
        }
        getXYColumnRow();
        System.out.println("Cards shuffled");
    }

    /**
     * Draws all the cards, face up or down.
     */
    public void draw(Graphics2D g2) {
        for (int i = 0; i < numberOfCards; i++) {
            BufferedImage image;
            if (cardset.get(i).flipped) {
                image = cardset.get(i).cardImage;
            } else {
                image = backOfCard;
            }
            g2.drawImage(image, cardset.get(i).cardX, cardset.get(i).cardY,
                    cardWidth, cardHeight, null);
        }
    }

    /**
     * Calculates each card's row/column and on-screen position.
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
