package main;

import javax.swing.JFrame;

/**
 * main file.
 */
public class App {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("CBL card game draft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        GamePanel blah = new GamePanel();
        frame.add(blah);
        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        blah.startGameThread();

    }
}
