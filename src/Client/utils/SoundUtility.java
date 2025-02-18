package Client.utils;

import javafx.scene.media.MediaPlayer;

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
    AudioInputStream audioInputStream;
    static String filePath;

    private static MediaPlayer mediaPlayer;
    public SoundUtility() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // create AudioInputStream object
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        // create clip reference
        clip = AudioSystem.getClip();
        // open audioInputStream to the clip
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Play the background music
    public static void playBackgroundMusic() {
        try
        {
            filePath = "resources/audio/bgm.wav";
            SoundUtility audioPlayer =
                    new SoundUtility();
            audioPlayer.play();
            Scanner sc = new Scanner(System.in);

            sc.close();
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
    // Stop the background music
    public static void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}


