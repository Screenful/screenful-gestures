package screenful.gui.rendering;

import com.primesense.nite.*;
import java.awt.*;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import screenful.basic.HandsListener;

import org.openni.*;

/**
 * Draw something with the hand tracker data. (Adapted from NiTE examples)
 */
public class HandsRenderer extends Component implements HandsListener {

    float[] histogram;
    int[] depthPixels;
    HandTracker tracker;
    HandTrackerFrameRef lastFrame;
    BufferedImage bufferedImage;
    int[] colors;

    public HandsRenderer(HandTracker hands) {
        tracker = hands;
        colors = new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF};
    }

    /**
     * Draw depth image and tracked hands
     */
    @Override
    public synchronized void paint(Graphics g) {
        if (lastFrame == null) {
            return;
        }

        VideoFrameRef depthFrame = lastFrame.getDepthFrame();
        if (depthFrame != null) {
            int width = depthFrame.getWidth();
            int height = depthFrame.getHeight();

            // make sure we have enough room
            if (bufferedImage == null || bufferedImage.getWidth() != width || bufferedImage.getHeight() != height) {
                bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            }

            bufferedImage.setRGB(0, 0, width, height, depthPixels, 0, width);

            g.drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), null);
            // draw hands
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setStroke(new BasicStroke(6));
            g2.setColor(Color.BLUE);

            // allow for (almost) arbitrarily resized window
            double xscale = g2.getClipBounds().width / (double) width;
            double yscale = g2.getClipBounds().height / (double) height;

            for (HandData hand : lastFrame.getHands()) {
                if (hand.isTracking()) {
                    com.primesense.nite.Point2D<Float> pos = tracker.convertHandCoordinatesToDepth(hand.getPosition());
                    // draw rectangle with respect to window size vs. the 
                    // coordinates calculated from the depth frame
                    g2.drawRect((int) (xscale * pos.getX().intValue() - 6), (int) (yscale * pos.getY().intValue() - 6), 10, 10);
                }
            }
        }
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
    public synchronized void onNewHandsFrame(HandTrackerFrameRef frame) {
        if (lastFrame != null) {
            lastFrame.release();
            lastFrame = null;
        }

        lastFrame = frame;

        VideoFrameRef depthFrame = lastFrame.getDepthFrame();
        if (depthFrame != null) {
            ByteBuffer frameData = depthFrame.getData().order(ByteOrder.LITTLE_ENDIAN);

            // make sure we have enough room
            if (depthPixels == null || depthPixels.length < depthFrame.getWidth() * depthFrame.getHeight()) {
                depthPixels = new int[depthFrame.getWidth() * depthFrame.getHeight()];
            }

            calcHist(frameData);
            frameData.rewind();
            int pos = 0;
            while (frameData.remaining() > 0) {
                short depth = frameData.getShort();
                short pixel = (short) histogram[depth];
                depthPixels[pos] = 0x00FF00FF & (0xFF000000 | (pixel << 16) | (pixel << 8));
                pos++;
            }
        }
        repaint();
    }
}
