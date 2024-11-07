import java.io.File;
import javax.sound.sampled.*;

public class SoundEffect {
    private Clip clip;

    public SoundEffect(String filename) {
        setClip(filename);
    }

    public void setClip(String filename) {
        try {
            File audioFile = new File(filename);
            if (!audioFile.exists()) {
                System.out.println("Audio file not found: " + filename);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file: " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O error while loading: " + filename);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable for: " + filename);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void playLoop() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
