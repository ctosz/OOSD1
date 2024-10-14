import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/* gameEntity is an abstract class generalised object. Concrete implementations are Player, Wall, Dot and Ghost as
these all fit the "____ is a gameEntity" relationship */
public abstract class gameEntity extends Rectangle {

    private final Image pngImage;
    private final double startXPos; /* Starting position: for resetting e.g. reset Player when collides with Ghost */
    private final double startYPos;
    public gameEntity(Image pngImage, String csvLine) {


        super(ShadowPac.csvSplitLine(csvLine)[0], ShadowPac.csvSplitLine(csvLine)[1], pngImage.getWidth(), pngImage.getHeight());
        this.pngImage = pngImage;

        this.startXPos = ShadowPac.csvSplitLine(csvLine)[0];
        this.startYPos = ShadowPac.csvSplitLine(csvLine)[1];

    }

    @Override
    // for removing the dot that has been eaten
    public boolean equals(Object other) {
        return super.equals(other);
    }

    public void renderImage(int isPacOpen, double rotation) {

        pngImage.drawFromTopLeft(this.left(), this.top());
    }

    // direction: 0 = positive change, 1 = negative change
    public void changeXPos(int direction, int increment) {

        if (direction == ShadowPac.RIGHT) {
            Point newTopLeft = new Point((this.left() + increment), this.top()); // y stays the same
            this.moveTo(newTopLeft);
        }
        if (direction == ShadowPac.LEFT) {
            Point newTopLeft = new Point((this.left() - increment), this.top()); // y stays the same
            this.moveTo(newTopLeft);
        }
    }

    public void changeYPos(int direction, int increment) {

        if (direction == ShadowPac.DOWN) {
            Point newTopLeft = new Point(this.left(), (this.top() + increment)); // x stays the same
            this.moveTo(newTopLeft);
        }
        if (direction == ShadowPac.UP) {
            Point newTopLeft = new Point(this.left(), (this.top() - increment)); // x stays the same
            this.moveTo(newTopLeft);
        }
    }

    public void resetPosition() {

        Point newTopLeft = new Point(this.startXPos, this.startYPos);
        this.moveTo(newTopLeft);
    }

    /* Move collision check should be a gameEntity method below, rather than being in the ShadowPac class */
//    public boolean checkMoveCollision(gameEntity entity) {
//
//        return this.intersects(entity);
//    }


}



