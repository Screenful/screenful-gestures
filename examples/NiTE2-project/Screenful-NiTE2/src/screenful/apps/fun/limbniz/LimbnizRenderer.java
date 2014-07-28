package screenful.apps.fun.limbniz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicReference;
import screenful.gestures.Utilities.CardinalDirection;

/**
 * Draw Limbniz's parameter values.
 *
 */
public class LimbnizRenderer extends Component implements LimbnizListener {

    CardinalDirection direction = CardinalDirection.STABLE;
    AtomicReference<BeatParams> params = new AtomicReference<>(new BeatParams(0, 0, 0, 0));

    public void setParams(BeatParams params) {
        this.params.set(params);
    }

    public LimbnizRenderer() {
    }

    private void paintParams(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int width = this.getWidth();
        int height = this.getHeight();

        Color lightblue = new Color(50, 180, 210);

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);

        g2.setStroke(new BasicStroke(6));
        g2.setColor(lightblue);
        g2.setFont(new Font("Serif", Font.BOLD, 42));

        g2.drawString("a: " + params.get().a + " b: " + params.get().b + " c: " + params.get().c, width / 10, (int) (height / 2));
    }

    @Override
    public void paint(Graphics g) {
        paintParams(g);

    }

    @Override
    public void onBeatParams(BeatParams params) {
        this.params.set(params);
        repaint();
    }

}
