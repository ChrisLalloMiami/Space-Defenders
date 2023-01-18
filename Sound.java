import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound  {
    
    /**
     * Clip interface for loading audio data.
     */
    private Clip clip;
    
    /**
     * Constructor for Sound objects, setting up an input stream
     * with the given file name.
     * @param fileName The name of the audio file to load.
     */
    public Sound(String fileName) {
        try {
            AudioInputStream audioIn = AudioSystem
                    .getAudioInputStream(getClass().getResource(fileName));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Plays the given Sound to the user.
     */
    public void play() {
        if (clip == null) {
            return;
        }
        if (clip.isRunning()) {
            clip.stop();         
        }
        clip.setFramePosition(0);
        clip.start();
    }  
}
