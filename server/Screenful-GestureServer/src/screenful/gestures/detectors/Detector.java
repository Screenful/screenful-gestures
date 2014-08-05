package screenful.gestures.detectors;

/**
 * Interface for detectors, ie. an object that determines whether the movement
 * during two consecutive frames was appropriate (true / false).
 *
 */
public interface Detector {

    /**
     * Override to implement behavior.
     *
     * @param frames two consecutive frames
     * @return true if movement was appropriate
     */
    boolean detected(ConsecutiveFrames frames);

    Displacement getData();

}
