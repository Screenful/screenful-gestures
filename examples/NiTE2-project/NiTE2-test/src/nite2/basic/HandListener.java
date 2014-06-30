package nite2.basic;

import com.primesense.nite.HandTrackerFrameRef;

/**
 * Any class that wants hand data should implement this interface and add itself
 * to NuiTracker's hand tracker listeners.
 */
public interface HandListener {

    void onNewHandFrame(HandTrackerFrameRef frame);

}
