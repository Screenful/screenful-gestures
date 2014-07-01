package nite2.basic;

import com.primesense.nite.HandTrackerFrameRef;

/**
 * Any class that wants hand data should implement this interface and add itself
 * to NuiTracker's listeners.
 */
public interface HandsListener {

    void onNewHandsFrame(HandTrackerFrameRef frame);

}
