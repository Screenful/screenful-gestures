package nite2.gui.rendering;

import com.primesense.nite.*;
import java.awt.*;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import nite2.basic.BonesListener;
import org.openni.*;

/**
 * Draw stick characters from skeleton data on top of depth image
 */
public class BonesRenderer extends Component implements BonesListener {

    float[] histogram;
    int[] depthPixels;
    UserTracker tracker;
    UserTrackerFrameRef lastFrame;
    BufferedImage bufferedImage;
    int[] colors;

    public BonesRenderer(UserTracker skel) {
        tracker = skel;
        colors = new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF};
    }

    /**
     * Draw image and skeletons
     */
    @Override
    public synchronized void paint(Graphics g) {
        if (lastFrame == null) {
            return;
        }

        int framePosX = 0;
        int framePosY = 0;

        VideoFrameRef depthFrame = lastFrame.getDepthFrame();
        if (depthFrame != null) {
            int width = depthFrame.getWidth();
            int height = depthFrame.getHeight();

            // make sure we have enough room
            if (bufferedImage == null || bufferedImage.getWidth() != width || bufferedImage.getHeight() != height) {
                bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            }
            bufferedImage.setRGB(0, 0, width, height, depthPixels, 0, width);

            framePosX = (getWidth() - width) / 2;
            framePosY = (getHeight() - height) / 2;

            g.drawImage(bufferedImage, framePosX, framePosY, null);
        }

        for (UserData user : lastFrame.getUsers()) {
            if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
                drawLimb(g, framePosX, framePosY, user, JointType.HEAD, JointType.NECK);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_SHOULDER, JointType.LEFT_ELBOW);
                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_ELBOW, JointType.LEFT_HAND);

                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_SHOULDER, JointType.RIGHT_ELBOW);
                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_ELBOW, JointType.RIGHT_HAND);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_SHOULDER, JointType.RIGHT_SHOULDER);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_SHOULDER, JointType.TORSO);
                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_SHOULDER, JointType.TORSO);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_HIP, JointType.TORSO);
                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_HIP, JointType.TORSO);
                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_HIP, JointType.RIGHT_HIP);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_HIP, JointType.LEFT_KNEE);
                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_KNEE, JointType.LEFT_FOOT);

                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_HIP, JointType.RIGHT_KNEE);
                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_KNEE, JointType.RIGHT_FOOT);
            }
        }
    }

    /**
     * Draw a single limb
     */
    private void drawLimb(Graphics g, int x, int y, UserData user, JointType from, JointType to) {
        com.primesense.nite.SkeletonJoint fromJoint = user.getSkeleton().getJoint(from);
        com.primesense.nite.SkeletonJoint toJoint = user.getSkeleton().getJoint(to);

        if (fromJoint.getPositionConfidence() == 0.0 || toJoint.getPositionConfidence() == 0.0) {
            return;
        }

        com.primesense.nite.Point2D<Float> fromPos = tracker.convertJointCoordinatesToDepth(fromJoint.getPosition());
        com.primesense.nite.Point2D<Float> toPos = tracker.convertJointCoordinatesToDepth(toJoint.getPosition());

        // draw it in another color than the use color
        g.setColor(new Color(colors[(user.getId() + 1) % colors.length]));
        // Draw thicker lines
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(new BasicStroke(6));
        g2.drawLine(x + fromPos.getX().intValue(), y + fromPos.getY().intValue(), x + toPos.getX().intValue(), y + toPos.getY().intValue());
    }

    private void calcHist(ByteBuffer depthBuffer) {
        // make sure we have enough room
        if (histogram == null) {
            histogram = new float[10001];
        }

        // reset
        for (int i = 0; i < histogram.length; ++i) {
            histogram[i] = 0;
        }

        int points = 0;
        while (depthBuffer.remaining() > 0) {
            int depth = depthBuffer.getShort() & 0xFFFF;
            if (depth != 0) {
                histogram[depth]++;
                points++;
            }
        }

        for (int i = 1; i < histogram.length; i++) {
            histogram[i] += histogram[i - 1];
        }

        if (points > 0) {
            for (int i = 1; i < histogram.length; i++) {
                histogram[i] = (int) (256 * (1.0f - (histogram[i] / (float) points)));
            }
        }
    }

    @Override
    public synchronized void onNewBonesFrame(UserTrackerFrameRef frame) {
        if (lastFrame != null) {
            lastFrame.release();
            lastFrame = null;
        }

        lastFrame = frame;

        VideoFrameRef depthFrame = lastFrame.getDepthFrame();

        if (depthFrame != null) {
            ByteBuffer frameData = depthFrame.getData().order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer usersFrame = lastFrame.getUserMap().getPixels().order(ByteOrder.LITTLE_ENDIAN);

            // make sure we have enough room
            if (depthPixels == null || depthPixels.length < depthFrame.getWidth() * depthFrame.getHeight()) {
                depthPixels = new int[depthFrame.getWidth() * depthFrame.getHeight()];
            }

            calcHist(frameData);
            frameData.rewind();
            int pos = 0;
            while (frameData.remaining() > 0) {
                short depth = frameData.getShort();
                short userId = usersFrame.getShort();
                short pixel = (short) histogram[depth];
                int color = 0xFFFFFFFF;
                if (userId > 0) {
                    color = colors[userId % colors.length];
                }

                depthPixels[pos] = color & (0xFF000000 | (pixel << 16) | (pixel << 8) | pixel);
                pos++;
            }
        }
        repaint();
    }
}
