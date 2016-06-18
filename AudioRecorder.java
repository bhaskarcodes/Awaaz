
import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioRecorder {
    TargetDataLine targetline;
    File audioFile = new File("record.wav");
    ArrayList<Thread> threadList = new ArrayList<Thread>();
    public void record() {
        System.out.println("started");
        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false); // for wav file
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("line not supported");
            }
            targetline = (TargetDataLine) AudioSystem.getLine(info);
            targetline.open();
            targetline.start();

            Thread thread = new Thread() {
                public void run() {
                    AudioInputStream audioStream = new AudioInputStream(targetline);
                    try {
                        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
                    } catch (IOException ex) {
                        Logger.getLogger(AudioRecorder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            thread.start();
            threadList.add(thread);
        } catch (Exception e) {
        }
    }
    public void stop(){
        targetline.stop();
        targetline.close();
    }

    public void stop(String filename) throws IOException, UnsupportedAudioFileException {
        final File myFile = new File(filename);
        Path FROM = Paths.get(audioFile.getPath());
        Path TO = Paths.get(myFile.getPath());
        Files.copy(FROM, TO, StandardCopyOption.REPLACE_EXISTING);
        audioFile.delete();  //DELETE BUFFER FILE
      /* 
        talking tom effect after sems...
        
        final File myFile1 = new File("ct.wav");
        AudioInputStream in = getAudioInputStream(myFile);
        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat(); 
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                50000,
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                baseFormat.isBigEndian());
        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        AudioSystem.write(din, AudioFileFormat.Type.WAVE, myFile1);
    */}

    }


