package screenful.detectors;

import screenful.gestures.Displacement;

/**
 * Interface for detectors, ie. an object that determines whether the movement
 * during two consecutive frames was appropriate (true / false).
 *
 */
public interface Detector {

    boolean detected(ConsecutiveFrames frames);

    Displacement getData();

}
