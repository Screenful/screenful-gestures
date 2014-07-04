package screenful.gestures;

import javafx.geometry.Point3D;
import screenful.gestures.Utilities.CardinalDirection;

/**
 * Extra data related to a detected gesture.
 */
public class GestureData {

    CardinalDirection direction;
    Point3D directionVector;

    public GestureData(CardinalDirection direction) {
        this.direction = direction;
    }

    public GestureData(Point3D directionVector, CardinalDirection direction) {
        this.direction = direction;
        this.directionVector = directionVector;
    }

    public GestureData() {
    }

    public Point3D getDirectionVector() {
        return directionVector;
    }

    public void setDirectionVector(Point3D directionVector) {
        this.directionVector = directionVector;
    }

    public void setDirection(CardinalDirection dir) {
        direction = dir;
    }

    public CardinalDirection getDirection() {
        return direction;
    }
}
