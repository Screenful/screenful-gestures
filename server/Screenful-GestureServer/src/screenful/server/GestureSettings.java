package screenful.server;

import com.primesense.nite.GestureType;
import java.util.HashSet;
import screenful.gestures.Utilities.CardinalDirection;

/**
 * Directional gesture detection parameter container. exitDirections and
 * enabledDirections define which directions send events to connected browsers.
 *
 */
public class GestureSettings {

    public int travelDistance;
    public int travelFrames;
    public int cooldown;
    public int startDelay;
    public HashSet<CardinalDirection> exitDirections;
    public HashSet<CardinalDirection> enabledDirections;

    public GestureSettings() {
        this.exitDirections = new HashSet<CardinalDirection>();
        this.enabledDirections = new HashSet<CardinalDirection>();
    }

    public GestureSettings(int td, int tf, int cd, int sd,
            HashSet<CardinalDirection> exitd,
            HashSet<CardinalDirection> ed,
            HashSet<GestureType> sg) {
        this.travelDistance = td;
        this.travelFrames = tf;
        this.cooldown = cd;
        this.startDelay = sd;
        this.exitDirections = exitd;
        this.enabledDirections = ed;
    }

}
