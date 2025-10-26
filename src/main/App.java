package main;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Main entry; creates window and starts GamePanel.
 */
public class App {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("Sam and Khion - CBL Card Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gp = new GamePanel();
        frame.add(gp);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gp.startGameThread();

        JFrame guide = new JFrame("Help - CBL Card Game");
        guide.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        guide.setResizable(false);
        JLabel label = new JLabel();
        label.setText("<html>Card Game Guide: Welcome to Sam and Khion's CBL Game.<br/>"
            + "<br/>In this game, you play as a Jester who is tasked with finding 8 pairs of cards."
            + "<br/>You must find all pairs of matching number."
            + "<br/>If you try flipping two Kings or Queens, they won't be too happy however."
            + "<br/>Use your WASD keys to move, and the spacebar to interact with cards,"
            + "<br/>or swing your sword.<br/>"
            + "<br/>Press the escape button at any time to reset your game."
            + "</html>");
        guide.add(label);
        guide.pack();
        guide.setLocationRelativeTo(frame);
        guide.setVisible(true);
    }
}
