package javacv.skeleton;

import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.bytedeco.javacpp.freenect.FREENECT_DEPTH_REGISTERED;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenKinectFrameGrabber;

public class ShowDepth {

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
                frame.showImage(depthImage);
            }
            frame.dispose();
            grabber.stop();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(ShowDepth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
