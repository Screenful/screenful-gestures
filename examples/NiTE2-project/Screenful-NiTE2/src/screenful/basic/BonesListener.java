package screenful.basic;

import com.primesense.nite.UserTrackerFrameRef;

/**
 * Any class that wants skeleton data should implement this interface and add
 * itself to NiTETracker's listeners.
 */
public interface BonesListener {

    void onNewBonesFrame(UserTrackerFrameRef frame);

}
