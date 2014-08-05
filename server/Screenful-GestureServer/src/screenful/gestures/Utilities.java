package screenful.gestures;

import com.primesense.nite.Point3D;

/**
 * Some general utility methods. OpenNI, NiTE and JavaFX all contain a class
 * called Point3D, but the JavaFX one is more versatile, so it is used whenever
 * possible.
 */
public final class Utilities {

    /**
     * Define cardinal directions
     */
    public enum CardinalDirection {

        STABLE, LEFT, RIGHT, DOWN, UP, IN, OUT
    }

    private Utilities() {
    }

    /**
     * For readability, round coordinates
     *
     * @param nitepoint NiTE's Point3D point
     * @return JavaFX Point3D point, rounded to integers
     */
    public static javafx.geometry.Point3D convertPoint(Point3D nitepoint) {
        return new javafx.geometry.Point3D(
                Math.round((float) nitepoint.getX()),
                Math.round((float) nitepoint.getY()),
                Math.round((float) nitepoint.getZ()));
    }

    /**
     * Calculate euclidean distance between two 3D points.
     *
     * @param from 3D start point
     * @param to 3D end point
     * @return distance between the points
     */
    public static double distance3d(Point3D from, Point3D to) {
        return convertPoint(from).distance(convertPoint(to));
    }

    /**
     * Return displacement vector from starting point to endpoint (simple
     * subtraction). Note that the returned Point3D is JavaFX, the handled
     * Point3Ds are from NiTE.
     *
     * @param from beginning Point3D
     * @param to ending Point3D
     * @return JavaFX Point3D of a vector pointing from the beginning point to
     * ending point
     */
    public static javafx.geometry.Point3D displacementVector(Point3D from, Point3D to) {
        javafx.geometry.Point3D start, end;
        start = convertPoint(from);
        end = convertPoint(to);
        return end.subtract(start);
    }

    /**
     * Detect general direction of a vector.
     *
     * @param vector general 3D vector
     * @param minSensitivity minimum threshold for magnitude to consider the
     * vector's direction
     * @return the cardinal direction of the vector
     */
    public static CardinalDirection determineCardinalDirection(javafx.geometry.Point3D vector, int minSensitivity) {
        // ignore minimal movement
        if (vector.magnitude() < minSensitivity) {
            return CardinalDirection.STABLE;
        }

        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        double absX = Math.abs(x);
        double absY = Math.abs(y);
        double absZ = Math.abs(z);

        // find axis with most magnitude
        // if X bigger than Y and Z
        if (absX >= absY && absX >= absZ) {
            return x < 0 ? CardinalDirection.LEFT : CardinalDirection.RIGHT;
            // if Y bigger than X and Z
        } else if (absY >= absX && absY >= absZ) {
            return y < 0 ? CardinalDirection.DOWN : CardinalDirection.UP;
        } else {
            // otherwise Z
            return z < 0 ? CardinalDirection.IN : CardinalDirection.OUT;
        }

    }

}
