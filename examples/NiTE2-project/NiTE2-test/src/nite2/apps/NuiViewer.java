package nite2.apps;

import nite2.basic.NuiTracker;
import nite2.gui.visualization.HandVisualization;
import nite2.gui.visualization.SkeletonVisualization;

/**
 * Views both input methods (skeleton / hand) simultaneously. Program exits when
 * both windows have been closed with Escape.
 */
public class NuiViewer {

    public static void main(String[] args) {
        // Create a tracker (starts automatically)
        NuiTracker tracker = new NuiTracker();
        // Add visualizations
        SkeletonVisualization skeleton = new SkeletonVisualization(tracker, "Skeleton tracker window");
        HandVisualization hands = new HandVisualization(tracker, "Hand tracker window");
        // Show visualizations
        skeleton.show();
        hands.show();
    }
}
