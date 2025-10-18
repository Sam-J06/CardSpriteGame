package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Class of sounds (music, sound effetcs...)
 */
public class Sound {

    Clip clip; //A Clip is used to open Audio Files.
    URL[] soundURL = new URL[4]; //Stores file paths of audio files.

    /**
     * Constructor.
     */
    public Sound() {
        //Sets file path.
        soundURL[0] = getClass().getResource("/res/sfx/flip.wav");
        soundURL[1] = getClass().getResource("/res/sfx/unflip.wav");
        soundURL[2] = getClass().getResource("/res/sfx/LOZ_Secret.wav");
        soundURL[3] = getClass().getResource("/res/sfx/combat.wav");
    }

    /**
     * Sets file in use to that of the desired URL.
     */
    public void setFile(int i) {
        try {
            
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * starts playing the clip called in GamePanel.
     */
    public void play() {
        clip.start();
    }

    /**
     * loops the clip called in GamePanel. Will be done for music, not sfx.
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * stops clip started in GamePanel.
     */
    public void stop() {
        clip.stop();
        System.out.println("stopped");
    }
    
}
