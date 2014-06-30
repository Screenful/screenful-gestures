package nite2.apps;

import com.primesense.nite.JointType;
import com.primesense.nite.Point3D;
import com.primesense.nite.SkeletonJoint;
import com.primesense.nite.UserData;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nite2.basic.NuiTracker;
import nite2.gui.visualization.HandVisualization;
import nite2.gui.visualization.SkeletonVisualization;

/**
 * Show tracked torso coordinates until Enter is pressed
 */
public class NuiHeadless {

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
                List<UserData> users = tracker.getSkeletons();
                if (users.size() > 0) {
                    for (UserData user : users) {
                        SkeletonJoint torso = user.getSkeleton().getJoint(JointType.TORSO);
                        Point3D pos = torso.getPosition();
                        if (torso.getPositionConfidence() != 0.0) {
                            System.out.printf("Torso #%d: (%6d %6d %6d)\n",
                                    user.getId(), Math.round((float) pos.getX()), Math.round((float) pos.getY()), Math.round((float) pos.getZ()));
                        }
                    }
                }
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            Logger.getLogger(NuiHeadless.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
