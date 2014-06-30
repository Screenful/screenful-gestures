package nite2.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Generic frame for displaying graphics. Can be closed by pressing Escape.
 */
public class GenericWindow implements Runnable {

    protected JFrame viewFrame;
    private boolean shouldRun = true;

    public GenericWindow(String name) {
        viewFrame = new JFrame(name);

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
