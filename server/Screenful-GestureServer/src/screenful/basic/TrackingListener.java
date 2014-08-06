package screenful.basic;

/**
 * As NiTE hand tracker lacks its own mechanism of notifying when tracking
 * starts or stops, a class can implement TrackingListener to get the events.
 *
 */
public interface TrackingListener {

    /**
     * Called when hand tracking has been started.
     */
    void onHandTrackingStarted();

    /**
     * Called when hand tracking stops (there are no more tracked hands).
     */
    void onHandTrackingStopped();
}
