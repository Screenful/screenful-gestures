package screenful.detectors;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import screenful.gestures.Displacement;

/**
 * Not finished, barely started! DOES NOT WORK!
 */
public class RecordingDetector implements Detector {

    private ArrayList<Displacement> reference;
    private int currentIndex;
    private int gesturetime;
    private int resolution;

    public RecordingDetector(int gesturetime, int resolution) {
        currentIndex = 0;
        this.gesturetime = gesturetime;
        this.resolution = resolution;
    }

    public RecordingDetector(int gesturetime, int resolution, String filename) {
        this(gesturetime, resolution);
        loadRecording(filename);
    }

    public final synchronized void loadRecording(String filename) {
        try {
            try (FileInputStream fis = new FileInputStream(filename);
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                reference = (ArrayList<Displacement>) ois.readObject();
            }
        } catch (IOException ioe) {
            System.out.println("IO exception loading recording.");
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
        }
    }

    @Override
    public boolean detected(ConsecutiveFrames frames) {

        // frames / gesturedata?
        return false;
    }

    @Override
    public Displacement getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
