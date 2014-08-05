package screenful.gui.visualization;

import screenful.gui.rendering.HandsRenderer;
import screenful.basic.NiTETracker;
import screenful.gui.GenericWindow;

/**
 * Hand tracker visualization window
 */
public class HandsVisualization extends GenericWindow implements Visualization {

    HandsRenderer renderer;

    @Override
    public void show() {
        renderer.setSize(640, 480);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
        new Thread(this).start();
    }

    public HandsVisualization(NiTETracker tracker, String name) {
        super(name);
        renderer = new HandsRenderer(tracker.getHandTracker());
        tracker.addHandsListener(renderer);
    }

}
