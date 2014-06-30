package nite2.gui.visualization;

import nite2.gui.rendering.HandRenderer;
import nite2.basic.NuiTracker;
import nite2.gui.GenericWindow;

/**
 * Hand tracker visualization window
 */
public class HandVisualization extends GenericWindow implements Visualization {

    HandRenderer renderer;

    public void show() {
        renderer.setSize(800, 600);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
        new Thread(this).start();
    }

    public HandVisualization(NuiTracker nui, String name) {
        super(name);
        renderer = new HandRenderer(nui.getHandTracker());
        nui.addHandListener(renderer);
    }

}
