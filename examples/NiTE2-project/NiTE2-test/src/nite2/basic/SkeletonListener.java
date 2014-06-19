package nite2.basic;

import com.primesense.nite.UserTrackerFrameRef;

/**
 * Any class that wants skeleton data should implement this interface and add
 * itself to SkeletonTracker listeners.
 */
public interface SkeletonListener {

    void onNewSkeletonFrame(UserTrackerFrameRef frame);

}
