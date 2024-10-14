import bagel.Image;

public class Life extends Image {

    public Life(String filename) {

        super(filename);
    }

    public void renderLife(int topLeftX, int topLeftY) {

        this.drawFromTopLeft(topLeftX, topLeftY);
    }

}
