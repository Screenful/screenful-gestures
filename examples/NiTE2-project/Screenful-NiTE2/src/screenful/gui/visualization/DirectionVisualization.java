package screenful.gui.visualization;

import screenful.gestures.Gesture;
import screenful.gui.GenericWindow;
import screenful.gui.rendering.DirectionRenderer;

/**
 * Directional gesture visualization window
 */
public class DirectionVisualization extends GenericWindow implements Visualization {

    DirectionRenderer renderer;

    public void show() {
        renderer.setSize(320, 240);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
        new Thread(this).start();
    }

    public DirectionVisualization(Gesture direction, String name) {
        super(name);
        renderer = new DirectionRenderer();
        direction.addListener(renderer);
    }

}
