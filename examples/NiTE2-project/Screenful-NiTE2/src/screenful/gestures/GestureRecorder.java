package screenful.gestures;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Record a gesture by saving a chain of movement. DOES NOT WORK, BARELY
 * STARTED.
 */
public class GestureRecorder implements GestureListener {

    private final ArrayList<Displacement> gestureSequence;
    private final int gesturetime;
    private final int resolution;
    private final int timestep;

    private boolean recording;
    private long startTime;
    private long lastRecordedTime;

    public GestureRecorder(int gesturetime, int resolution) {
        this.gestureSequence = new ArrayList<>();
        this.gesturetime = gesturetime;
        this.resolution = resolution;
        this.timestep = gesturetime / resolution;
        this.recording = false;
        this.lastRecordedTime = 0;
    }

    public void record() {
        gestureSequence.clear();
        recording = true;
        startTime = System.currentTimeMillis();
        System.out.println("Started recording");
    }

    public void saveGesture(String filename) {
        if (gestureSequence.isEmpty()) {
            System.out.println("saveGesture - No recorded gesture in memory.");
            return;
        }
        if (recording) {
            System.out.println("saveGesture - Can't save while recording.");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gestureSequence);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            System.out.println("IO exception when writing file " + filename);
            return;
        }
    }

    @Override
    public void onGesture(Displacement gesture) {
        if (recording) {
            if (System.currentTimeMillis() > startTime + gesturetime) {
                recording = false;
                System.out.println("Finished recording (" + (System.currentTimeMillis() - startTime) + " ms), list size = " + gestureSequence.size());
            }
            // even if recording finished, add this last data
            if (System.currentTimeMillis() - timestep > lastRecordedTime) {
                gestureSequence.add(gesture);
                lastRecordedTime = System.currentTimeMillis();
            }
        }
    }
}
