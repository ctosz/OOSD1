import bagel.DrawOptions;
import bagel.Image;


public class Player extends gameEntity {

    private final DrawOptions ROTATE_PAC = new DrawOptions(); /* Ed#221 */

    /* Player has two possible pngs */
    private final Image PAC; /* This isn't the most efficient code - doubling up with Superclass pngImage
                                causing double rendering of Player at random (?) */
    private final Image PAC_OPEN;

    public Player(Image pac, Image pacOpen, String csvLine) {

        super(pac, csvLine);

        this.PAC = pac;
        this.PAC_OPEN = pacOpen;

    }

    @Override
    public void renderImage(int isPacOpen, double rotation) {

        if (isPacOpen == 0) {
            /* Render closed */
            PAC.drawFromTopLeft(this.left(), this.top(), ROTATE_PAC.setRotation(rotation));
        }
        if (isPacOpen == 1) {
            /* Render open */
            PAC_OPEN.drawFromTopLeft(this.left(), this.top(), ROTATE_PAC.setRotation(rotation));
        }
    }

}
