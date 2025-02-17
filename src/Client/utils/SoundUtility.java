package Client.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

public class SoundUtility {

    private static MediaPlayer mediaPlayer;

    // Play the background music
    public static void playBackgroundMusic(String musicFileName) {
        try {
            // Stop the old music if it's playing
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();  // Dispose to release resources
            }
            // Dynamically create the path based on the given file name
            String musicPath = "resources/audio/bgm.wav" + musicFileName;

            String bgmPath = SoundUtility.class.getResource(musicPath).toExternalForm();

            if (bgmPath == null) {
                System.err.println("Error: Could not find the background music file: " + musicFileName);
                return;  // Exit if the file is not found
            }

            System.out.println("Playing BGM from: " + bgmPath);

            Media bgm = new Media(bgmPath);
            mediaPlayer = new MediaPlayer(bgm);

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);  // Loop indefinitely
            mediaPlayer.play();

        } catch (MediaException e) {
            System.err.println("Error while initializing the MediaPlayer: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("Error: Media file path is incorrect or the file is missing.");
        }
    }

    // Stop the background music
    public static void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}


