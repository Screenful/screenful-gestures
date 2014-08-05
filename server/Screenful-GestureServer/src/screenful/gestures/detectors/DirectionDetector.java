package screenful.gestures.detectors;

import com.primesense.nite.HandData;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point3D;
import static screenful.gestures.Utilities.CardinalDirection.STABLE;
import static screenful.gestures.Utilities.determineCardinalDirection;
import static screenful.gestures.Utilities.displacementVector;

/**
 * Detects hand point movement direction by calculating the displacement vector
 * of each hand point (could also use skeletons) between two consecutive frames.
 * When any tracked hand performs a big enough movement (sensitivity set in
 * millimeters), the detected(..) method returns true.
 *
 * This class is used by the Gesture class for defining the logic of detecting a
 * gesture.
 *
 * Note that currently the displacement data is saved only from a single hand
 * (to be read with getData()), however any detected hand can trigger a command.
 * This means that if during a single frame step multiple hands are moving, only
 * the last one's data is passed to the gesture listener as the direction. This
 * could be improved.
 */
public class DirectionDetector implements Detector {

    Displacement data;
    int sensitivity;
    // Use HashMap to match hand IDs properly
    HashMap<Short, HandData> previousHandMap;

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public DirectionDetector(int sensitivity) {
        data = new Displacement();
        previousHandMap = new HashMap<>();
        this.sensitivity = sensitivity;
    }

    /**
     * The actual detection logic.
     *
     * @param frames a ConsecutiveFrames containing the last two user and hand
     * tracker frames.
     * @return true if displacement of a hand is appropriate between the two
     * frames
     */
    @Override
    public boolean detected(ConsecutiveFrames frames) {

        // avoid error when first frame is null (if bones frames are used, check for those too)
        if (frames.previousHandsFrame == null) {
            return false;
        }

        List<HandData> currentHands = frames.handsFrame.getHands();
        for (HandData current : currentHands) {
            // if hand was seen in previous frame (by id number)
            if (previousHandMap.containsKey(current.getId())) {
                // get the data from previous frame
                HandData previous = previousHandMap.get(current.getId());
                // calculate displacement
                Point3D displacementVector = displacementVector(previous.getPosition(), current.getPosition());
                // refresh current data
                previousHandMap.put(current.getId(), current);
                // note that only one hand's data is saved to be queried
                this.data = new Displacement(displacementVector, determineCardinalDirection(displacementVector, sensitivity), current.getId());
                // return direction only if there was one
                if (getData().getDirection() != STABLE) {
                    return true;
                }
            }
            previousHandMap.put(current.getId(), current);
        }
        return false;
    }

    /**
     * Return current data.
     *
     * @return displacement data of the last detected hand motion
     */
    @Override
    public synchronized Displacement getData() {
        return data;
    }
}
