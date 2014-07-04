package screenful.gui.rendering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import screenful.gestures.GestureData;
import screenful.gestures.GestureListener;
import screenful.gestures.Utilities.CardinalDirection;

/**
 * Draw some feedback to directional gestures.
 *
 */
public class DirectionRenderer extends Component implements GestureListener {

    CardinalDirection direction = CardinalDirection.STABLE;

    public DirectionRenderer() {
    }

    @Override
    public void onGesture(GestureData gesture) {
        direction = gesture.getDirection();
        repaint();
    }

    private void directionText(Graphics2D g2, String text) {
        g2.drawString(text, 100, this.getHeight() - (this.getHeight() / 4));
    }

    private void paintDirection(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        int width = this.getWidth();
        int height = this.getHeight();

        Color lightblue = new Color(30, 150, 180);

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);

        g2.setStroke(new BasicStroke(6));
        g2.setColor(lightblue);
        g2.setFont(new Font("Serif", Font.BOLD, height / 8));
        System.out.println("" + direction.toString());
        switch (direction) {
            case LEFT:
                directionText(g2, "left");
                break;
            case RIGHT:
                directionText(g2, "right");
                break;
            case UP:
                directionText(g2, "up");
                break;
            case DOWN:
                directionText(g2, "down");
                break;
            case IN:
                g2.setColor(Color.GREEN);
                directionText(g2, "in");
                break;
            case OUT:
                g2.setColor(Color.RED);
                directionText(g2, "out");
                break;
        }
    }

    @Override
    public void paint(Graphics g) {
        paintDirection(g);
    }

}
