package screenful.apps.fun.limbniz;

import screenful.gui.GenericWindow;
import screenful.gui.visualization.Visualization;

/**
 * Limbniz parameter visualization window
 */
public class LimbnizVisualization extends GenericWindow implements Visualization {

    LimbnizRenderer renderer;

    public void show() {
        renderer.setSize(640, 480);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
        new Thread(this).start();
    }

    public LimbnizVisualization(LimbnizController controller, String name) {
        super(name);
        renderer = new LimbnizRenderer();
        controller.addListener(renderer);
    }

}
