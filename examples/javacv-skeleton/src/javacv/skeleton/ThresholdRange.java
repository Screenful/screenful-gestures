package javacv.skeleton;

import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.bytedeco.javacpp.freenect.FREENECT_DEPTH_REGISTERED;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenKinectFrameGrabber;

public class ThresholdRange {

    /**
     * Return thresholded depth image, include only depth samples between min_mm
     * and max_mm (mm).
     *
     * @param src source image
     * @return
     */
    private static IplImage threshold(IplImage src, int min_mm, int max_mm) {
        IplImage result = IplImage.create(src.width(), src.height(), src.depth(), src.nChannels());
        ShortBuffer input = src.getShortBuffer();
        ShortBuffer output = result.getShortBuffer();

        // Inspect every pixel
        while (input.remaining() > 0) {
            int val = input.get();
            if (val >= min_mm && val < max_mm) {
                output.put((short) (val));
            } else {
                output.put((short) 0);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        OpenKinectFrameGrabber grabber = new OpenKinectFrameGrabber(0);
        // Depth format: millimeters and registered with RGB image
        grabber.setDepthFormat(FREENECT_DEPTH_REGISTERED);
        // IMPORTANT FOR KINECT! (some models only?)
        grabber.setByteOrder(ByteOrder.LITTLE_ENDIAN);

        IplImage depthImage;
        CanvasFrame frame = new CanvasFrame("Kinect depth image", CanvasFrame.getDefaultGamma() / grabber.getGamma());

        try {
            grabber.start();
            while (frame.isVisible() && (depthImage = grabber.grabDepth()) != null) {
                int centerDepth = depthImage.getShortBuffer().get(depthImage.width() / 2 + (depthImage.height() / 2 * depthImage.width()));
                if (centerDepth != 0) {
                    System.out.println("Depth at center: " + centerDepth + " mm");
                }
                // Threshold depth image to contain only samples whose depth
                // is between certain limits (millimeters)
                frame.showImage(threshold(depthImage, 0, 1000));
            }
            frame.dispose();
            grabber.stop();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(ThresholdRange.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
