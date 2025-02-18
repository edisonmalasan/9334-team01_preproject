package Client.utils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundUtility {
    // to store current position
    Long currentFrame;
    Clip clip;
    // current status of clip
    String status;
    AudioInputStream in1, in2;
    static String bgmPath, clickPath;

    public SoundUtility() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // create AudioInputStream object
        in1 = AudioSystem.getAudioInputStream(new File(bgmPath).getAbsoluteFile());
        in2 = AudioSystem.getAudioInputStream(new File(clickPath).getAbsoluteFile());

        // create clip reference
        clip = AudioSystem.getClip();
        // open audioInputStream to the clip
        clip.open(in1);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Play the background music
    public static void playBackgroundMusic() {
        try {
            bgmPath = "resources/audio/bgm.wav";
            SoundUtility audioPlayer1 = new SoundUtility();
            audioPlayer1.play();
            Scanner sc = new Scanner(System.in);

            sc.close();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public static void playClickingSound() {
        try {
            clickPath = "resources/audio/Click.wav";
            SoundUtility audioPlayer2 = new SoundUtility();
            audioPlayer2.play();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
        public void play() {
            //start the clip
            clip.start();
            status = "play";
    }
}


