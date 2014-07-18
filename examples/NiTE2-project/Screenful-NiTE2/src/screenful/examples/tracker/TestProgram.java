package screenful.examples.tracker;

import java.util.logging.Level;
import java.util.logging.Logger;
import screenful.basic.NiTETracker;
import screenful.detectors.DirectionDetector;
import screenful.gestures.Gesture;
import screenful.gestures.Utilities.CardinalDirection;
import screenful.gui.visualization.HandsVisualization;

/**
 * A fairly minimal example on how to connect different listeners together.
 *
 * First a NiTETracker is created and it will immediately start analyzing the
 * hand and skeleton frames.
 *
 * Then, a gesture for directional movement is created by creating a gesture
 * with a DirectionDetector and passing the sensitivity parameters. Instances of
 * LastDirection and HandSeparation are created. Now all the necessary objects
 * exist.
 *
 * To connect the listeners, we add the directions gesture to the tracker's
 * hands listeners, the hand separation checker to its bones listeners, and
 * additionally add the LastDirection object to the directions gesture's
 * listeners, ie. it will store the last value if/when a directional swipe is
 * detected.
 *
 * To spice things up, a hand visualization window is shown and the program
 * begins an infinite loop where the console will likely get flooded with the
 * millimeter distance between your hands (if it sees your skeleton and your
 * hands are not near each other) or the direction of the last directional swipe
 * motion of a tracked hand.
 */
public class TestProgram {

    public static void main(String[] args) {
        // Create tracker and begin looking for features.
        NiTETracker tracker = new NiTETracker();

        // Set 10 for millimeter sensitivity, 5 for required frames, 0 ms cooldown between gestures.
        Gesture directions = new Gesture(new DirectionDetector(10), 5, 0);

        // Create objects that use the tracker and gestures in some way.
        // LastDirection just saves the last detected direction in its direction field.
        LastDirection lastdirection = new LastDirection();
        // HandSeparation prints the distance between hands of a user when they're apart (> 50 cm)
        HandSeparation handseparation = new HandSeparation();

        // Add directional gesture to the tracker's hands listeners.
        tracker.addHandsListener(directions);
        // Add hand separation checker to the tracker's bones listeners.
        tracker.addBonesListener(handseparation);
        // Add lastdirection to the directional gesture's listeners, so when a 
        // direction is gestured, it can be read from lastdirection.direction.
        directions.addListener(lastdirection);
        // Create a hand visualization.
        HandsVisualization hands = new HandsVisualization(tracker, "Hand tracker window");
        // Show the visualization window on screen.
        hands.show();

        CardinalDirection lastPrinted = null;
        // Infinite loop to see how it all works out.
        while (true) {
            // Don't spam the console too much.
            if (lastPrinted != lastdirection.direction) {
                lastPrinted = lastdirection.direction;
                System.out.println("" + lastPrinted);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestProgram.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
