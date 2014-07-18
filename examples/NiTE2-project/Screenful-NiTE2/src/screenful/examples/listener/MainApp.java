package screenful.examples.listener;

public class MainApp {

    public static void main(String[] args) {
        // Create object that listens, in this case a class 
        // that prints out the number it's notified with.
        NumberPrinter listener = new NumberPrinter();
        // Create object that notifies listeners.
        Notifier notifier = new Notifier();
        // Add listener object to notifier object's list of listeners.
        notifier.addListener(listener);
        // Start doing something with the notifier object,
        // in this case, it implements Runnable, so we spawn
        // a new thread to run it.
        new Thread(notifier).start();
    }

}
