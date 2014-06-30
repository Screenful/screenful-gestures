package nite2.gui.visualization;

import nite2.gui.rendering.SkeletonRenderer;
import nite2.basic.NuiTracker;
import nite2.gui.GenericWindow;

/**
 * Skeleton tracker visualization window
 */
public class SkeletonVisualization extends GenericWindow implements Visualization {

    SkeletonRenderer renderer;

    @Override
    public void show() {
        renderer.setSize(800, 600);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
        new Thread(this).start();
    }

    public SkeletonVisualization(NuiTracker nui, String name) {
        super(name);
        renderer = new SkeletonRenderer(nui.getUserTracker());
        nui.addSkeletonListener(renderer);
    }

}
