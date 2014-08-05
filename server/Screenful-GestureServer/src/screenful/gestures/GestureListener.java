package screenful.gestures;

import screenful.gestures.detectors.Displacement;

/**
 * A class that wants notifications when a gesture is detected should implement
 * GestureListener and add itself to a gesture's listeners.
 *
 */
public interface GestureListener {

    /**
     * Override to implement behavior.
     *
     * @param gesture displacement data of the gesture
     */
    void onGesture(Displacement gesture);
}
