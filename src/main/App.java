package main;

import javax.swing.JFrame;

/**
 * Main entry; creates window and starts GamePanel.
 */
public class App {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("CBL card game draft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gp = new GamePanel();
        frame.add(gp);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gp.startGameThread();
    }
}
