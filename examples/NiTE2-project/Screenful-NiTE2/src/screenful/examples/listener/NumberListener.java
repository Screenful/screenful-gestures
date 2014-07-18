package screenful.examples.listener;

/**
 * Interface for a listener that receives an integer when notified.
 */
public interface NumberListener {

    /**
     * A notifying class should call onNewData(i) for all its listeners when
     * desired conditions apply, eg. notifyListeners() method that iterates
     * through the list of listeners and calls onNewData(i) for each.
     *
     * @param i integer argument to pass as extra data when notify occurs
     */
    void onNewData(int i);
}
