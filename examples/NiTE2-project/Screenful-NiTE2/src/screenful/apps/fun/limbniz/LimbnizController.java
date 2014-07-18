package screenful.apps.fun.limbniz;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import screenful.basic.NiTETracker;
import screenful.detectors.DirectionDetector;
import screenful.gestures.Gesture;
import screenful.gestures.Displacement;
import screenful.gestures.GestureListener;
import screenful.gestures.Utilities.CardinalDirection;

    //GestureData current = newDisplacementa(Point3D.ZERO, Utilities.CardinalDirection.STABLE);
public class LimbnizController implements GestureListener {

    private final AtomicReference<BeatParams> params;

    public BeatParams getParams() {
        return params.get();
    }

    private final Gesture direction;
    private final ArrayList<LimbnizListener> listeners;

    public LimbnizController(NiTETracker tracker, int magnitude, int length, int cooldown) {
        this.params = new AtomicReference<>(new BeatParams(0, 0, 0, 0));
        // make controller very sensitive
        direction = new Gesture(new DirectionDetector(magnitude), length, cooldown);
        tracker.addHandsListener(direction);
        direction.addListener(this);
        listeners = new ArrayList<>();
    }

    public void addListener(LimbnizListener listener) {
        listeners.add(listener);
    }

    public void incrementTime() {
        BeatParams cur = params.get();
        this.params.set(new BeatParams(cur.t + 1, cur.a, cur.b, cur.c));
    }

    @Override
    public void onGesture(Displacement gesture) {
        double increment = 1;
        CardinalDirection movement = gesture.getDirection();
        BeatParams cur = params.get();
        switch (movement) {
            case STABLE:
                break;
            case LEFT:
                params.set(new BeatParams(cur.t, (int) (cur.a - increment), cur.b, cur.c));
                break;
            case RIGHT:
                params.set(new BeatParams(cur.t, (int) (cur.a + increment), cur.b, cur.c));
                break;
            case UP:
                params.set(new BeatParams(cur.t, cur.a, (int) (cur.b + increment), cur.c));
                break;
            case DOWN:
                params.set(new BeatParams(cur.t, cur.a, (int) (cur.b - increment), cur.c));
                break;
            case IN:
                params.set(new BeatParams(cur.t, cur.a, cur.b, (int) (cur.c + increment)));
                break;
            case OUT:
                params.set(new BeatParams(cur.t, cur.a, cur.b, (int) (cur.c - increment)));
                break;
        }

        notifyListeners();

    }

    private void notifyListeners() {
        for (LimbnizListener listener : listeners) {
            listener.onBeatParams(params.get());
        }
    }
}
