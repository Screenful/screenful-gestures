package screenful.examples.tracker;

import screenful.gestures.Displacement;
import screenful.gestures.GestureListener;
import screenful.gestures.Utilities.CardinalDirection;

/**
 * Very simple listener that just saves the direction in a public field when it
 * gets notified with a gesture.
 *
 */
public class LastDirection implements GestureListener {

    CardinalDirection direction = CardinalDirection.STABLE;

    public LastDirection() {
    }

    @Override
    public synchronized void onGesture(Displacement gesture) {
        direction = gesture.getDirection();
    }

}
