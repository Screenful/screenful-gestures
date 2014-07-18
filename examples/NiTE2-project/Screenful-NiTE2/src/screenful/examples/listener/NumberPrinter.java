package screenful.examples.listener;

public class NumberPrinter implements NumberListener {

    @Override
    public void onNewData(int i) {
        System.out.println("" + i);
    }

}
