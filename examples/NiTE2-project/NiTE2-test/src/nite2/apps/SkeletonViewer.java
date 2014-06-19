package nite2.apps;

import nite2.basic.SkeletonTracker;

public class SkeletonViewer {

    public static void main(String[] args) {
        // Create a tracker and open a viewer window for it
        SkeletonTracker tracker = new SkeletonTracker();
        tracker.showImageWindow();
    }
}
