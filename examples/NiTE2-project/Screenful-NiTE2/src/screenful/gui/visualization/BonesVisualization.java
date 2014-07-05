package screenful.gui.visualization;

import screenful.gui.rendering.BonesRenderer;
import screenful.basic.NiTETracker;
import screenful.gui.GenericWindow;

/**
 * Skeleton tracker visualization window
 */
public class BonesVisualization extends GenericWindow implements Visualization {

    BonesRenderer renderer;

    @Override
    public void show() {
        renderer.setSize(320, 240);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
        new Thread(this).start();
    }

    public BonesVisualization(NiTETracker nui, String name) {
        super(name);
        renderer = new BonesRenderer(nui.getUserTracker());
        nui.addBonesListener(renderer);
    }

}
