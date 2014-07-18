package screenful.apps.fun;

import screenful.basic.NiTETracker;
import screenful.detectors.DirectionDetector;
import screenful.gestures.Gesture;
import screenful.gestures.Displacement;
import screenful.gestures.GestureListener;
import screenful.gui.visualization.DirectionVisualization;
import screenful.gui.visualization.HandsVisualization;

/**
 * Just a fun program to play different drum sounds by moving hands in different
 * directions. It doesn't allow for very speedy playing, only one instance per
 * clip can be playing at the same time.
 *
 */
public class AirDrum {

    static class DrumSounds implements GestureListener {

        // initialize sound
        public DrumSounds() {
            SoundEffect.init();
        }

        /**
         * Play sounds according to the direction of the gesture.
         *
         * @param gesture gesture data
         */
        @Override
        public void onGesture(Displacement gesture) {
            switch (gesture.getDirection()) {
                case LEFT:
                    SoundEffect.LEFT.play();
                    break;
                case RIGHT:
                    SoundEffect.RIGHT.play();
                    break;
                case UP:
                    SoundEffect.UP.play();
                    break;
                case DOWN:
                    SoundEffect.DOWN.play();
                    break;
                case IN:
                    SoundEffect.IN.play();
                    break;
                case OUT:
                    SoundEffect.OUT.play();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        // create tracker, gesture and direction detector for gesture, use very high sensitivity
        NiTETracker tracker = new NiTETracker();
        // 10 for millimeter sensitivity, 5 for required frames, 0 ms cooldown between gestures
        Gesture directions = new Gesture(new DirectionDetector(10), 5, 0);
        // remember to add gesture to tracker's listeners
        tracker.addHandsListener(directions);
        // create drum and add it to gesture listeners
        DrumSounds drums = new DrumSounds();
        directions.addListener(drums);
        // Start some visualization windows
        HandsVisualization hands = new HandsVisualization(tracker, "Hand tracker window");
        DirectionVisualization directionsVis = new DirectionVisualization(directions, "Hand direction window");
        hands.show();
        directionsVis.show();
    }
}
