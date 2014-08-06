package screenful.testapps;

import screenful.basic.NiTETracker;
import screenful.gui.visualization.HandsVisualization;
import screenful.gui.visualization.BonesVisualization;

/**
 * Shows two graphical windows, one for hand tracker and one for user tracker.
 * Program exits when both windows have been closed with Escape.
 */
public class BonesAndHandsViewer {

    public static void main(String[] args) {
        // Create a tracker (starts automatically)
        NiTETracker tracker = new NiTETracker(true, true);
        // Add visualizations
        BonesVisualization bones = new BonesVisualization(tracker, "Skeleton tracker window");
        HandsVisualization hands = new HandsVisualization(tracker, "Hand tracker window");
        // Show visualizations
        bones.show();
        hands.show();
    }
}
