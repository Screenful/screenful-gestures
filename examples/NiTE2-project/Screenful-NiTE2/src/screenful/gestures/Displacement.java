package screenful.gestures;

import javafx.geometry.Point3D;
import screenful.gestures.Utilities.CardinalDirection;

/**
 * Extra data related to the movement of a point (joint, hand) between two
 * frames.
 */
public class Displacement {

    CardinalDirection direction;
    Point3D directionVector;
    short id;

    public short getId() {
        return id;
    }

    public Displacement(CardinalDirection direction) {
        this.direction = direction;
    }

    public Displacement(Point3D directionVector, CardinalDirection direction) {
        this.direction = direction;
        this.directionVector = directionVector;
    }

    public Displacement(Point3D directionVector, CardinalDirection direction, short id) {
        this.direction = direction;
        this.directionVector = directionVector;
        this.id = id;
    }

    public Displacement() {
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
