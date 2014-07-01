package nite2.gui.visualization;

import nite2.gui.rendering.BonesRenderer;
import nite2.basic.NuiTracker;
import nite2.gui.GenericWindow;

/**
 * Skeleton tracker visualization window
 */
public class BonesVisualization extends GenericWindow implements Visualization {

    BonesRenderer renderer;

    @Override
    public void show() {
        renderer.setSize(800, 600);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
        new Thread(this).start();
    }

    public BonesVisualization(NuiTracker nui, String name) {
        super(name);
        renderer = new BonesRenderer(nui.getUserTracker());
        nui.addBonesListener(renderer);
    }

}
