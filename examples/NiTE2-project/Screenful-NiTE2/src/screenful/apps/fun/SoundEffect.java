package screenful.apps.fun;

import java.io.*;
import javax.sound.sampled.*;

/**
 * Adapted from:
 * http://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
 */
public enum SoundEffect {

    LEFT("res/left.wav"),
    RIGHT("res/right.wav"),
    UP("res/up.wav"),
    DOWN("res/down.wav"),
    IN("res/in.wav"),
    OUT("res/out.wav");

    public static enum Volume {

        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;

    private Clip clip;

    SoundEffect(String soundFileName) {
        try {
            File url = new File(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
    }

    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Optional static method to pre-load all the sound files.
    static void init() {
        values();
    }
}
