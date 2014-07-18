package screenful.detectors;

import com.primesense.nite.HandData;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point3D;
import screenful.gestures.Displacement;
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
 */
public class DirectionDetector implements Detector {

    Displacement data;
    int sensitivity;

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    // Use HashMap to match hand IDs properly
    HashMap<Short, HandData> previousHandMap;

    public DirectionDetector(int sensitivity) {
        data = new Displacement();
        previousHandMap = new HashMap<>();
        this.sensitivity = sensitivity;
    }

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
                this.data = new Displacement(displacementVector, determineCardinalDirection(displacementVector, sensitivity));
                // return direction only if there was one
                if (getData().getDirection() != STABLE) {
                    return true;
                }
            }
            previousHandMap.put(current.getId(), current);
        }
        return false;
    }

    @Override
    public synchronized Displacement getData() {
        return data;
    }
}
