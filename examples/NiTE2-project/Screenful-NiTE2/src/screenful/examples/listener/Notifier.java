package screenful.examples.listener;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Notifier implements Runnable {

    ArrayList<NumberListener> listeners;
    int i;

    public Notifier() {
        listeners = new ArrayList<>();
        i = 0;
    }

    public void addListener(NumberListener lis) {
        listeners.add(lis);
    }

    private void notifyListeners() {
        for (NumberListener lis : listeners) {
            lis.onNewData(i);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Notifier.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
            notifyListeners();
        }
    }

}
