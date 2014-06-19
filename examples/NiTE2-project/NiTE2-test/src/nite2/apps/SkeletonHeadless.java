package nite2.apps;

import java.util.logging.Level;
import java.util.logging.Logger;
import nite2.basic.SkeletonTracker;

/**
 * Simply loop until user tracking is started, then exit
 */
public class SkeletonHeadless {

    public static void main(String[] args) {
        SkeletonTracker tracker = new SkeletonTracker();
        while (tracker.getSkeletons().isEmpty()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(SkeletonHeadless.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("" + tracker.getSkeletons().size());
    }
}
