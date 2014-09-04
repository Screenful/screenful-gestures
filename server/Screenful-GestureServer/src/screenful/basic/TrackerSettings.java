package screenful.basic;

import com.primesense.nite.GestureType;
import java.util.HashSet;

/**
 * Container class for NiTETracker settings.
 *
 */
public class TrackerSettings {

    public boolean handTrackerEnabled;
    public boolean skeletonTrackerEnabled;
    public HashSet<GestureType> startGestures;

    public TrackerSettings() {
        this.startGestures = new HashSet<>();
        this.handTrackerEnabled = true;
        this.skeletonTrackerEnabled = true;
    }

    public TrackerSettings(boolean hte, boolean ste, HashSet<GestureType> sg) {
        this.startGestures = new HashSet<>();
        this.handTrackerEnabled = hte;
        this.skeletonTrackerEnabled = ste;
        this.startGestures = sg;
    }
}
