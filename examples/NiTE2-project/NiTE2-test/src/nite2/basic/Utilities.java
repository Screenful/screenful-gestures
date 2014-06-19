package nite2.basic;

import com.primesense.nite.Point3D;

/**
 * Some general utility methods
 */
public final class Utilities {

    private Utilities() {
    }

    /**
     * Calculate euclidean distance between two 3D points
     */
    public static double distance3d(Point3D from, Point3D to) {
        float dx = Math.round((float) to.getX()) - Math.round((float) from.getX());
        float dy = Math.round((float) to.getY()) - Math.round((float) from.getY());
        float dz = Math.round((float) to.getZ()) - Math.round((float) from.getZ());
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

}
