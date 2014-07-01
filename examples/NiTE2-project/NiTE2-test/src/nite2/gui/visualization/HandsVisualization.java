package nite2.gui.visualization;

import nite2.gui.rendering.HandsRenderer;
import nite2.basic.NuiTracker;
import nite2.gui.GenericWindow;

/**
 * Hand tracker visualization window
 */
public class HandsVisualization extends GenericWindow implements Visualization {

    HandsRenderer renderer;

    public void show() {
        renderer.setSize(800, 600);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
        new Thread(this).start();
    }

    public HandsVisualization(NuiTracker nui, String name) {
        super(name);
        renderer = new HandsRenderer(nui.getHandTracker());
        nui.addHandsListener(renderer);
    }

}
