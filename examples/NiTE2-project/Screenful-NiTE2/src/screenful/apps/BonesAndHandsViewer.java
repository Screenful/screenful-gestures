package screenful.apps;

import screenful.basic.NiTETracker;
import screenful.gui.visualization.HandsVisualization;
import screenful.gui.visualization.BonesVisualization;

/**
 * Views both input methods (skeleton / hand) simultaneously. Program exits when
 * both windows have been closed with Escape.
 */
public class BonesAndHandsViewer {

    public static void main(String[] args) {
        // Create a tracker (starts automatically)
        NiTETracker tracker = new NiTETracker();
        // Add visualizations
        BonesVisualization bones = new BonesVisualization(tracker, "Skeleton tracker window");
        HandsVisualization hands = new HandsVisualization(tracker, "Hand tracker window");
        // Show visualizations
        bones.show();
        hands.show();
    }
}
