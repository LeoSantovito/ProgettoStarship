package org.example.swing;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class MusicPlayer {

    private Clip clip;


    public MusicPlayer(String musicFilePath) throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                new File(musicFilePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
    }

    public void playMusic() {
        clip.start();
    }

    public void stopMusic() {
        clip.stop();
    }

    public void loopMusic() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void setVolume(float volume) {
        if (clip == null) {
            return;
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }



}
