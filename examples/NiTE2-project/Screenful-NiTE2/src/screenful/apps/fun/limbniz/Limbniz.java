package screenful.apps.fun.limbniz;

import javax.sound.sampled.*;
import screenful.basic.NiTETracker;
import screenful.gui.visualization.HandsVisualization;

/**
 * Experimental "bytebeat" controller, unfinished and tacky for now. When the
 * program starts, two windows will spawn, one for hand tracker depth image and
 * one for beat parameters a, b and c.
 *
 * The program simply squeals numbers out of the speakers, and uses a time
 * counter that increases with each tick. For 8 kHz sound, the data would be
 * played 8000 times a second. The actual noise comes from simple (or complex)
 * functions that use the time counter and the parameters as input and return a
 * number. typically using arithmetic and bitwise operations to generate
 * repeating and evolving patterns that may be "music", or perhaps something
 * else. Modifying the parameters in real-time lets one experiment with the
 * sounds.
 *
 * To start controlling the beat, make a "click" gesture towards the sensor,
 * preferably at around 1.5 meters or more and with the sensor in front of you.
 * The hand tracker window will show a blue rectangle tracking the hand when you
 * have control.
 *
 * The program uses a DirectionDetector to look for movement in the cardinal
 * directions (left, right, up, down, in, out) within the space perceived by the
 * sensor. For accurate control, move the hand in straight lines to one of the
 * directions. The sensitivity can be controlled with the parameters in the
 * controller's constructor.
 *
 * Controls:
 *
 * Right-Left - a++, a--
 *
 * Up-Down - b++, b--
 *
 * In-Out - c++, c--
 *
 * Pressing Enter in the console rotates between a few functions, not all of
 * them very good.
 *
 * For now it uses only the hand tracker, but could potentially utilize the
 * X-Y-Z directional movement of all the 15 joint positions times the number of
 * people + the movement of all tracked hands. If a directional movement in one
 * of the six directions is considered a command, a single skeleton could be
 * used to trigger 90 of them. The effects of such a number of inputs may need
 * some thought in the function, but would probably be interesting, as would
 * making formulas that make use of complex motions a human can make.
 *
 * Limbniz is not based on but is inspired by IBNIZ by Ville-Matias Heikkilä:
 * http://pelulamu.net/ibniz/
 */
public class Limbniz {

    public static void main(String[] args) throws Exception {

        NiTETracker tracker = new NiTETracker();
        // Create new controller with 5 millimeter sensitivity, 
        // 2 frame length and 0 ms cooldown.
        LimbnizController controller = new LimbnizController(tracker, 5, 2, 0);

        // Create some beats.
        //Beats.Blippy blip = new Beats.Blippy();
        Beats.Darkbeat darkbeat = new Beats.Darkbeat();
        //Beats.HardAlgo hardalgo = new Beats.HardAlgo();
        Beats.GlitchCore glitchcore = new Beats.GlitchCore();
        Beats.Synth synth = new Beats.Synth();
        Beats.SimpleSound simplesound = new Beats.SimpleSound();
        int beatSelection = 0;

        // Spawn visualizations.
        HandsVisualization handsvis = new HandsVisualization(tracker, "Hand tracker window");
        LimbnizVisualization spacevis = new LimbnizVisualization(controller, "Beat parameters");
        handsvis.show();
        spacevis.show();

        //AudioFormat format = new AudioFormat(22050f, 8, 1, false, false);
        // Using 8 kHz for extra grit.
        AudioFormat format = new AudioFormat(8000f, 8, 1, false, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
        // set small (256 byte) buffer for better feedback
        soundLine.open(format, 256);
        soundLine.start();

        byte[] buffer = new byte[8];
        int t = 0;
        BeatParams params;
        while (true) {
            if (System.in.available() > 0) {
                while (System.in.available() > 0) {
                    System.in.read();
                }
                beatSelection++;
                if (beatSelection > 3) {
                    beatSelection = 0;
                }
            }
            for (int n = 0; n < buffer.length; n++) {

                params = controller.getParams();
                switch (beatSelection) {
                    case 0:
                        buffer[n] = (byte) (int) darkbeat.apply(params);
                        break;
                    case 1:
                        buffer[n] = (byte) (int) simplesound.apply(params);
                        break;
                    case 2:
                        buffer[n] = (byte) (int) synth.apply(params);
                        break;
                    case 3:
                        buffer[n] = (byte) (int) glitchcore.apply(params);
                        break;

                }
                controller.incrementTime();
            }
            soundLine.write(buffer, 0, buffer.length);
        }
    }
}
