package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Handles keyboard and mouse input.
 */
public class KeyHandler implements KeyListener, MouseListener {

    //wasd
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;

    //other
    public boolean escapePressed;
    public boolean spacePressed;
    public boolean cPressed;
    public boolean mPressed;

    //arrow
    public boolean uPressed;
    public boolean dPressed;
    public boolean lPressed;
    public boolean rPressed;

    // true only while left mouse button is held
    public boolean mousePressed;

    // mouse position for click handling (used for Try Again button)
    public int mouseX;
    public int mouseY;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            escapePressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
        if (code == KeyEvent.VK_C) {
            cPressed = true;
        }
        if (code == KeyEvent.VK_UP) {
            uPressed = true;
        }
        if (code == KeyEvent.VK_DOWN) {
            dPressed = true;
        }
        if (code == KeyEvent.VK_LEFT) {
            lPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rPressed = true;
        }
        if (code == KeyEvent.VK_M) {
            mPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            escapePressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
        if (code == KeyEvent.VK_C) {
            cPressed = false;
        }
        if (code == KeyEvent.VK_UP) {
            uPressed = false;
        }
        if (code == KeyEvent.VK_DOWN) {
            dPressed = false;
        }
        if (code == KeyEvent.VK_LEFT) {
            lPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rPressed = false;
        }
        if (code == KeyEvent.VK_M) {
            mPressed = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        if (e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = true;
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
        if (e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = false;
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
