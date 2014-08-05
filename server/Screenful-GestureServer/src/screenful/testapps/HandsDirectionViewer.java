package screenful.testapps;

import screenful.basic.NiTETracker;
import screenful.gestures.detectors.DirectionDetector;
import screenful.gestures.Gesture;
import screenful.gui.visualization.DirectionVisualization;
import screenful.gui.visualization.HandsVisualization;

/**
 * Show general direction of tracked hand movement
 */
public class HandsDirectionViewer {

    public static void main(String[] args) {
        // create the tracker
        NiTETracker tracker = new NiTETracker();

        // create a gesture 
        Gesture directions = new Gesture(new DirectionDetector(5), 10, 50);

        // add directional gesture detector to NiTETracker's listeners
        tracker.addHandsListener(directions);

        // Add something to listen to the gestures
        // directions.addListener(...);
        // draw depth image and tracked hands
        HandsVisualization hands = new HandsVisualization(tracker, "Hand tracker window");
        // visualize detected directional gestures
        DirectionVisualization directionsVis = new DirectionVisualization(directions, "Hand direction window");

        // show visualization windows
        hands.show();
        directionsVis.show();
    }
}
