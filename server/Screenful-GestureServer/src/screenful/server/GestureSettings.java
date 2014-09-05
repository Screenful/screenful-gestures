package screenful.server;

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
        this.exitDirections = new HashSet<>();
        this.enabledDirections = new HashSet<>();
    }

    public GestureSettings(int travelDistance, int travelFrames, int cooldown, int startDelay,
            HashSet<CardinalDirection> exitDirections,
            HashSet<CardinalDirection> enabledDirections) {
        this.travelDistance = travelDistance;
        this.travelFrames = travelFrames;
        this.cooldown = cooldown;
        this.startDelay = startDelay;
        this.exitDirections = exitDirections;
        this.enabledDirections = enabledDirections;
    }

}
