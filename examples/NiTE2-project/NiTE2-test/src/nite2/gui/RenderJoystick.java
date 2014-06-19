package nite2.gui;

import com.primesense.nite.UserData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import nite2.basic.SkeletonTracker;
import nite2.gestures.JointMetrics;
import static nite2.gestures.JointMetrics.elbowHandXOffset;

/**
 * Render a joystick-like view of X difference of hands and elbows
 *
 */
public class RenderJoystick extends GraphicsRenderer {

    public RenderJoystick(SkeletonTracker tracker) {
        super(tracker);
    }

    @Override
    public synchronized void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(80, 80, 200));
        g2.setStroke(new BasicStroke(6));
        int centerX = getWidth() / 2;
        for (UserData user : lastFrame.getUsers()) {
            double offset = elbowHandXOffset(user, JointMetrics.Side.LEFT);
            g2.drawLine(centerX, getHeight() - 1, (int) Math.round(centerX + offset), 0);
            offset = elbowHandXOffset(user, JointMetrics.Side.RIGHT);
            g2.setColor(new Color(200, 80, 80));
            g2.drawLine(centerX, getHeight() - 1, (int) Math.round(centerX + offset), 0);

        }
    }

}
