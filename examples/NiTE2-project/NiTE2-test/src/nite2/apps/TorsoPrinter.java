package nite2.apps;

import com.primesense.nite.JointType;
import com.primesense.nite.Point3D;
import com.primesense.nite.SkeletonJoint;
import com.primesense.nite.UserData;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nite2.basic.NuiTracker;
import static nite2.gestures.Poses.dorkyClick;
import nite2.gui.visualization.BonesVisualization;
import nite2.gui.visualization.HandsVisualization;

/**
 * Show tracked torso coordinates until Enter is pressed
 */
public class TorsoPrinter {

    public static void main(String[] args) {
        NuiTracker tracker = new NuiTracker();
        // uncomment for visualization windows
        /*
         // Add visualizations
         SkeletonVisualization skeleton = new SkeletonVisualization(tracker, "Skeleton tracker window");
         HandVisualization hands = new HandVisualization(tracker, "Hand tracker window");
         // Show visualizations
         skeleton.show();
         hands.show();
         */

        System.out.println("*** Press ENTER to quit.");
        try {
            while (System.in.available() == 0) {
                List<UserData> users = tracker.getBones();
                if (users.size() > 0) {
                    for (UserData user : users) {
                        SkeletonJoint torso = user.getSkeleton().getJoint(JointType.TORSO);
                        Point3D pos = torso.getPosition();
                        if (torso.getPositionConfidence() != 0.0) {
                            System.out.printf("Torso #%d: (%6d %6d %6d)\n",
                                    user.getId(), Math.round((float) pos.getX()), Math.round((float) pos.getY()), Math.round((float) pos.getZ()));
                        }
                        if (dorkyClick(user)) {
                            click();
                        }
                    }
                }
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            Logger.getLogger(TorsoPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void click() {
        System.out.println("       _________________\n"
                + "      |.---------------.|\n"
                + "      ||    _          ||\n"
                + "      ||   |_)| |\\ /   ||\n"
                + "      ||   |_)|_/ |    ||\n"
                + "      ||          ,    ||\n"
                + "      ||__________|`.__||\n"
                + " jgs  '-----------|_ r--'\n"
                + "                    \\\n"
                + "                     `\n");
    }
}
