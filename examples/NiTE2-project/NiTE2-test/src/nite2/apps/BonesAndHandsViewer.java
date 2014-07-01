package nite2.apps;

import nite2.basic.NuiTracker;
import nite2.gui.visualization.HandsVisualization;
import nite2.gui.visualization.BonesVisualization;

/**
 * Views both input methods (skeleton / hand) simultaneously. Program exits when
 * both windows have been closed with Escape.
 */
public class BonesAndHandsViewer {

    public static void main(String[] args) {
        // Create a tracker (starts automatically)
        NuiTracker tracker = new NuiTracker();
        // Add visualizations
        BonesVisualization bones = new BonesVisualization(tracker, "Skeleton tracker window");
        HandsVisualization hands = new HandsVisualization(tracker, "Hand tracker window");
        // Show visualizations
        bones.show();
        hands.show();
    }
}
