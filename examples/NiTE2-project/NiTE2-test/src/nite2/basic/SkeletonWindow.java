package nite2.basic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Window frame for drawing the depth image and skeleton joints
 */
public class SkeletonWindow implements Runnable {

    private final JFrame viewFrame;
    private final SkeletonRender renderer;
    private boolean shouldRun = true;

    public SkeletonRender getSkeletonRender() {
        return renderer;
    }

    public SkeletonWindow(SkeletonTracker skel) {
        viewFrame = new JFrame("NiTE User Tracker Viewer");
        renderer = new SkeletonRender(skel);

        // register to key events
        viewFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    shouldRun = false;
                }
            }
        });

        // register to closing event
        viewFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                shouldRun = false;
            }
        });

        renderer.setSize(800, 600);
        viewFrame.add("Center", renderer);
        viewFrame.setSize(renderer.getWidth(), renderer.getHeight());
        viewFrame.setVisible(true);
    }

    @Override
    public void run() {

        while (shouldRun) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        viewFrame.dispose();
    }
}
