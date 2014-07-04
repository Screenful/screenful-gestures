package screenful.gui.visualization;

import screenful.gui.rendering.HandsRenderer;
import screenful.basic.NiTETracker;
import screenful.gui.GenericWindow;

/**
 * Hand tracker visualization window
 */
public class HandsVisualization extends GenericWindow implements Visualization {

    HandsRenderer renderer;

    public void show() {
        renderer.setSize(320, 240);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
        new Thread(this).start();
    }

    public HandsVisualization(NiTETracker nui, String name) {
        super(name);
        renderer = new HandsRenderer(nui.getHandTracker());
        nui.addHandsListener(renderer);
    }

}
