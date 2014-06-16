package javacv.skeleton;

import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacpp.Loader;
import static org.bytedeco.javacpp.freenect.FREENECT_DEPTH_REGISTERED;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvScalar;
import static org.bytedeco.javacpp.helper.opencv_core.AbstractCvScalar.ZERO;
import static org.bytedeco.javacpp.helper.opencv_core.cvDrawContours;
import org.bytedeco.javacpp.helper.opencv_imgproc;
import static org.bytedeco.javacpp.helper.opencv_imgproc.cvFindContours;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.CV_FILLED;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateSeq;
import static org.bytedeco.javacpp.opencv_core.cvCvtScaleAbs;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_NONE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_EXTERNAL;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_LIST;
import static org.bytedeco.javacpp.opencv_imgproc.cvCanny;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenKinectFrameGrabber;

/**
 * Play around a bit with the depth image
 *
 */
public class Contours {

    // Do something simple with the image.
    // Note naming of methods: "cvCanny", "cvCvtScaleAbs", ie. they start with "cv" in JavaCV.
    private static IplImage canny(IplImage img) {
        // create temporary image the same size as the input image
        IplImage tmp = cvCreateImage(cvSize(img.width(), img.height()), IPL_DEPTH_8U, 1);
        // downscale values from 16-bit range (actually 0-4096) to 8-bit range 0-255
        cvCvtScaleAbs(img, tmp, IPL_DEPTH_8U, 1 / 256.0);
        // create a destination image
        IplImage cannied = IplImage.create(cvGetSize(tmp), tmp.depth(), 0);
        // apply canny edge detection with some arbitrary values
        cvCanny(tmp, cannied, 100, 200, 3);
        return cannied;
    }

    public static void main(String[] args) {
        OpenKinectFrameGrabber grabber = new OpenKinectFrameGrabber(0);
        grabber.setDepthFormat(FREENECT_DEPTH_REGISTERED);
        // IMPORTANT FOR KINECT!
        // If you use another sensor or image values seem wrong, comment it out.
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
                frame.showImage(canny(depthImage));
            }
            frame.dispose();
            grabber.stop();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(Contours.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
