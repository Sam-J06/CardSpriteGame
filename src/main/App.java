package main;

import javax.swing.JFrame;

/**
 * This is the main file. The frame is created here. GamePanel is then created and updated.
 */
public class App {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("CBL card game draft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        //TODO figure out how to make game fullscreen or change scale by user.
        
        GamePanel gp = new GamePanel();
        frame.add(gp);
        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        gp.startGameThread();

    }
}
