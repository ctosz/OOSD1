import bagel.Font;
public class Message {
    private double bottomLeftX;
    private double bottomLeftY;
    private String message;
    private final Font FONT;

    public Message(double x, double y, String message, int fontSize) {
        bottomLeftX = x;
        bottomLeftY = y;
        this.message = message;

        FONT = new Font("res/FSO8BITR.TTF", fontSize);
    }

    public double getFontWidth() { return this.FONT.getWidth(this.message); }


    public void setMessage(String message) {
        this.message = message;
    }

    public void displayMessage() { this.FONT.drawString(this.message, this.bottomLeftX, this.bottomLeftY); }


}
