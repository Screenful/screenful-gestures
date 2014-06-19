package nite2.apps;

import nite2.basic.SkeletonTracker;
import nite2.gui.RenderJoystick;

/**
 * Draw two lines across the screen, one for each hand. X-distance between hand
 * and elbow determine the "angle" of the lines.
 */
public class ElbowJoystick {

    private static SkeletonTracker tracker;

    public static void main(String[] args) {
        tracker = new SkeletonTracker();
        // convoluted for now
        tracker.showGraphicsWindow(new RenderJoystick(tracker));
    }

}
