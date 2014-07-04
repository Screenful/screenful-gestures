package screenful.gestures;

/**
 * A class that wants notifications when a gesture is detected should implement
 * GestureListener and add itself to the gesture's listeners.
 *
 */
public interface GestureListener {

    void onGesture(GestureData gesture);
}
