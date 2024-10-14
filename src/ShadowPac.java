import bagel.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2023

 * Please enter your name below
 * Claire Tosolini

 * I seem to have implemented this project 50% procedurally and 50% object-oriented. WHO KNOWS HOW I MANAGED IT!!!!!!

 * I have tried to comment everywhere I see an issue with how to fix it (this is mainly for my own understanding,
 * so I can give fixing it up a good shot - but if it helps whoever marks this to understand where I'm coming from,
 * that would be a big bonus :D !)

 * I don't think I can fix it properly without breaking all my code and essentially having to redo the whole thing UGH,
 * but I will try.
 */
public class ShadowPac extends AbstractGame {

    /* Directional constants
    * Public because used in this class and in gameEntity */
    public static final int RIGHT = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int UP = 3;

    /* Rotational constants
    * These should be attributes of either Player (if just want Players to be able to rotate), or of all
    * gameEntities (if you want all entities to have the ability to rotate).
    */
    private static final double DEFAULT_ROTATION = Math.toRadians(0);
    private static final double DOWN_ROTATION = Math.toRadians(90);
    private static final double LEFT_ROTATION = Math.toRadians(180);
    private static final double UP_ROTATION = Math.toRadians(270);
    public static double CURRENT_ROTATION = Math.toRadians(0); /* Keeps track of current rotation of player */

    /* Game setup constants */
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int MAXIMUM_CSV_ENTRIES = 271;
    private static final int NUM_PLAYERS = 1;
    private static final int NUM_GHOSTS = 4;
    private static final int NUM_DOTS = 121;
    private static final int NUM_WALLS = 145;
    private static final int MAX_NUM_LIVES = 3;
    private static final int DOT_VALUE = 10; /* Amount of points one Dot is worth */
    private static final int WIN_SCORE = 1210; /* If playerScore equals this, the player has won */
    private static final int NO_LIVES = 0;

    /* Game message constants - Strings
    * Should move to Message class
    */
    private static final String DEFAULT_FONT = "res/FSO8BITR.TTF";
    private static final String GAME_TITLE_STRING = "SHADOW PAC";
    private static final String INSTRUCTION_MESSAGE_STRING = "PRESS SPACE TO START\nUSE ARROW KEYS TO MOVE";
    private static final String SCORE_MESSAGE_STRING = "SCORE ";
    private static final String SCORE_MESSAGE_STRING_ZERO = "SCORE 0";
    private static final String WIN_MESSAGE_STRING = "WELL DONE!";
    private static final String LOSE_MESSAGE_STRING = "GAME OVER!";

    /* Game message constants - font sizes */
    private final int DEFAULT_FONT_SIZE = 64;
    private final int INSTRUCTION_MESSAGE_FONT_SIZE = 24;
    private final int SCORE_MESSAGE_FONT_SIZE = 20;

    /* Font objects for each game message
    * Should move to Message class
    * */
//    private final Font GAME_TITLE_M_FONT = new Font(DEFAULT_FONT, DEFAULT_FONT_SIZE);
//    private final Font INSTRUCTION_M_FONT = new Font(DEFAULT_FONT, INSTRUCTION_MESSAGE_FONT_SIZE);
//    private final Font SCORE_M_FONT = new Font(DEFAULT_FONT, SCORE_MESSAGE_FONT_SIZE);
    private final Font WIN_M_FONT = new Font(DEFAULT_FONT, DEFAULT_FONT_SIZE);
    private final Font LOSE_M_FONT = new Font(DEFAULT_FONT, DEFAULT_FONT_SIZE);

    /* Image objects for each .png that will be rendered */
    private final Image PAC_PNG = new Image("res/pac.png");
    private final Image PAC_OPEN_PNG = new Image("res/pacOpen.png");
    private final Image WALL_PNG = new Image("res/wall.png");
    private final Image DOT_PNG = new Image("res/dot.png");
    private final Image GHOST_RED_PNG = new Image("res/ghostRed.png");
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    // private final Image HEART_PNG = new Image("res/heart.png");

    /* Game message coordinates
    * Should move to Message class
    * */
    private final int TITLE_MESSAGE_X_COORD = 260;
    private final int TITLE_MESSAGE_Y_COORD = 250;
    private final int INSTRUCTION_MESSAGE_X_PIXEL_INCREASE = 60;
    private final int INSTRUCTION_MESSAGE_Y_PIXEL_INCREASE = 190;
    private final int INSTRUCTION_MESSAGE_X = TITLE_MESSAGE_X_COORD + INSTRUCTION_MESSAGE_X_PIXEL_INCREASE;
    private final int INSTRUCTION_MESSAGE_Y = TITLE_MESSAGE_Y_COORD + INSTRUCTION_MESSAGE_Y_PIXEL_INCREASE;
    private final int SCORE_MESSAGE_X = 25;
    private final int SCORE_MESSAGE_Y = 25;
    private final int LEFT_HEART_X_COORD = 900; /* Leftmost heart; positions of others are derived from this
                                                    Heart-related constants should be in Life class */
    private final int HEARTS_Y_COORD = 10;
    private final int HEARTS_PIXEL_INCREASE = 30; /* Each heart increases by 30 pixels in the x direction */
    private final double CENTERED_MESSAGE_X_WIN = WINDOW_WIDTH/2.0 - WIN_M_FONT.getWidth(WIN_MESSAGE_STRING)/2;
    private final double CENTERED_MESSAGE_X_LOSE = WINDOW_WIDTH/2.0 - LOSE_M_FONT.getWidth(LOSE_MESSAGE_STRING)/2;
    private final double CENTERED_MESSAGE_Y = WINDOW_HEIGHT/2.0 + DEFAULT_FONT_SIZE/2.0;

    /* Message objects for each game message */
    private final Message TITLE_MESSAGE = new Message(TITLE_MESSAGE_X_COORD, TITLE_MESSAGE_Y_COORD, GAME_TITLE_STRING,
                                                    DEFAULT_FONT_SIZE);
    private final Message INSTRUCTION_MESSAGE = new Message(INSTRUCTION_MESSAGE_X, INSTRUCTION_MESSAGE_Y,
                                                    INSTRUCTION_MESSAGE_STRING, INSTRUCTION_MESSAGE_FONT_SIZE);
    private final Message SCORE_MESSAGE = new Message(SCORE_MESSAGE_X, SCORE_MESSAGE_Y, SCORE_MESSAGE_STRING_ZERO,
                                                    SCORE_MESSAGE_FONT_SIZE);
    private final Message WIN_MESSAGE = new Message(CENTERED_MESSAGE_X_WIN, CENTERED_MESSAGE_Y, WIN_MESSAGE_STRING,
                                                    DEFAULT_FONT_SIZE);
    private final Message LOSE_MESSAGE = new Message(CENTERED_MESSAGE_X_LOSE, CENTERED_MESSAGE_Y, LOSE_MESSAGE_STRING,
                                                    DEFAULT_FONT_SIZE);

    /* Variables shared across all instances of AbstractGame ShadowPac */
    private static int spacePressed = 0; /* 0 if not been pressed yet, 1 once it has. For removing title screen i.e.
                                            start the game when space pressed. Thank you, Stella Li (Ed #102)! */
    private static int frameCount = 0; /* increase by 1 each time player is rendered. When frameCount == 15,
                                            change Pac image (open/closed) and reset frameCount to 0 */
    private static int isPacOpen = 0; /* 0 is false, 1 is true */
    private static int playerLives = MAX_NUM_LIVES; /* Should be an attribute in Player (or gameEntity) class
                                                        - player HAS lives */
    private static int playerScore = 0; /* Should be an attribute in Player (or gameEntity) class
                                            - player HAS score */
    private static int totalGameEntities = MAXIMUM_CSV_ENTRIES; /* Changes whenever a dot is 'eaten'
                                                                    Move to gameEntity class */
    private static String scoreMessageString = SCORE_MESSAGE_STRING + playerScore; /* Probably also belongs in Player class */

    /* Define arrays */
    private ArrayList<gameEntity> gameEntities = new ArrayList<>(MAXIMUM_CSV_ENTRIES);
    private Life[] lives = new Life[MAX_NUM_LIVES]; /* Belongs in Player class: player HAS lives (attribute) */
    private gameEntity playerRef; /* Reference to the player - for easier access compared to using gameEntities.get(0)
                                    every time */


    public ShadowPac() {

        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE_STRING);

        /* Each line of CSV file becomes an element in the array */
        String[] arrayOfPositions = readCSV();

        instantiateGameEntities(arrayOfPositions);
    }

    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     * there are always a set number of player/ghosts/dots/walls (271 total entries)
     * return an array of all positions
     */
    private static String[] readCSV() {

        String[] arrayOfPositions = new String[MAXIMUM_CSV_ENTRIES];
        int lenArrayOfPositions = 0; /* Current number of lines, while not all have been read */

        /* Try-Catch condition from Week 4 Lectures */
        try (BufferedReader file = new BufferedReader(new FileReader("res/level0.csv"))) {
            String text;

            while ((text = file.readLine()) != null) {

                arrayOfPositions[lenArrayOfPositions] = text;
                lenArrayOfPositions += 1;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayOfPositions;
    }

    /**
     * Method for extracting the X and Y coordinates of a gameEntity from a CSV Line
     */
    public static double[] csvSplitLine(String csvLine) {

        double[] xyCoords = new double[2]; /* Array of length 2 since we are extracting 2 coordinates */

        String[] buffer = csvLine.split(",");
        String x = buffer[1]; /* Skip buffer[0] value as it is the name of the entity e.g. ghost, wall, dot etc. */
        String y = buffer[2];

        xyCoords[0] = Double.parseDouble(x);
        xyCoords[1] = Double.parseDouble(y);

        return xyCoords;
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPac game = new ShadowPac();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        int speed = 3; /* Number of pixels moved per frame */
        playerRef = gameEntities.get(0);

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        if (spacePressed == 0) {

            gameStart();
        }

        if (input.wasPressed(Keys.SPACE)) {

            spacePressed = 1;
        }

        if (spacePressed == 1) {

            renderGameEntities(); /* Render all game entities to the screen */

            pacImageLogic(); /* Changing Pac image so mouth opens and closes */

            renderLivesAndScore(playerLives);

            entityMovement(playerRef, input, speed); /* Player movement including move validation */

            winLoseDetection(); /* Check if game has been won or lost yet */

            frameCount += 1; /* Variable for pacImageLogic to use */
        }

    }

    /**
     * Method for instantiating the fixed number of game entities.
     * Positions are retrieved from CSV file.
     * According to Assignment 1 specification sheet: 1 player, 4 ghosts, 145 walls, 121 dots
     */
    private void instantiateGameEntities(String[] arrayOfPositions) {

        int currentIndexOfArray = 0;

        gameEntities.add(new Player(PAC_PNG, PAC_OPEN_PNG, arrayOfPositions[currentIndexOfArray]));
        currentIndexOfArray += 1;

        for (int i = 0; i < NUM_GHOSTS; i++) {

            gameEntities.add(new Ghost(GHOST_RED_PNG, arrayOfPositions[currentIndexOfArray]));
            currentIndexOfArray += 1;
        }

        // add walls
        for (int i = 0; i < NUM_WALLS; i++) {

            gameEntities.add(new Wall(WALL_PNG, arrayOfPositions[currentIndexOfArray]));
            currentIndexOfArray += 1;
        }

        for (int i = 0; i < NUM_DOTS; i++) {
            gameEntities.add(new Dot(DOT_PNG, arrayOfPositions[currentIndexOfArray]));
            currentIndexOfArray += 1;
        }
    }

    /**
     * Display title screen
     */
    private void gameStart() {

        TITLE_MESSAGE.displayMessage();
        INSTRUCTION_MESSAGE.displayMessage();
    }

    /**
     * Render all existing entities to the screen.
     * Number of entities changes as dots are eaten.
     */
    private void renderGameEntities() {

        for (int i = 0; i < totalGameEntities; i++) {

            gameEntities.get(i).renderImage(isPacOpen, CURRENT_ROTATION);
        }
    }


    /**
     * Renders players current lives and score to the screen.
     * This method should be in the Player class (or gameEntity, if other entities have lives).
     * Since the Player HAS lives and HAS score (has == attribute)
     * @param playerLives - number of lives changes e.g. if player collides with ghost.
     */
    private void renderLivesAndScore(int playerLives) {

        SCORE_MESSAGE.displayMessage();

        int xPos = LEFT_HEART_X_COORD;

        for (int i = 0; i < playerLives; i++) {

            lives[i] = new Life("res/heart.png");
            lives[i].renderLife(xPos, HEARTS_Y_COORD);

            xPos += HEARTS_PIXEL_INCREASE;
        }
    }


    /**
     * Method for player's mouth opening and closing - image rendered switches between 'open' and 'closed'
     * Should be a method in the Player class, if not in gameEntity (then any entity could have different image states)
     */
    private void pacImageLogic() {

        if ((isPacOpen == 0) && (frameCount == 15)) {

            /* Mouth currently closed. It has been closed for 15 frames, so change to open */
            isPacOpen = 1;
            playerRef.renderImage(isPacOpen, DEFAULT_ROTATION);
            frameCount = 0;
        }
        else if ((isPacOpen == 1) && (frameCount == 15)) {

            isPacOpen = 0;
            playerRef.renderImage(isPacOpen, DEFAULT_ROTATION);
            frameCount = 0;
        }
    }


    /**
     * Method for movement of an entity including move validation checks.
     * This method should be moved into gameEntity class
     * @param entity the entity you want to move
     * @param input key press
     * @param speed pixels moved per frame
     */
    private void entityMovement(gameEntity entity, Input input, int speed) {

        /* To move pac diagonally, change 'else if' statements to 'if' statements */

        if (input.isDown(Keys.RIGHT)) {

            entity.changeXPos(RIGHT, speed);

            /* check validity of rotation */
            checkMoveAndProceed(Keys.RIGHT, speed, entity);

            CURRENT_ROTATION = DEFAULT_ROTATION;
            /* rotate player */
            playerRef.renderImage(isPacOpen, CURRENT_ROTATION);
        }
        else if (input.isDown(Keys.LEFT)) {
            entity.changeXPos(LEFT, speed);
            checkMoveAndProceed(Keys.LEFT, speed, entity);

            CURRENT_ROTATION = LEFT_ROTATION;
            playerRef.renderImage(isPacOpen, CURRENT_ROTATION);
        }
        else if (input.isDown(Keys.DOWN)) {
            entity.changeYPos(DOWN, speed);
            checkMoveAndProceed(Keys.DOWN, speed, entity);

            CURRENT_ROTATION = DOWN_ROTATION;
            playerRef.renderImage(isPacOpen, CURRENT_ROTATION);
        }
        else if (input.isDown(Keys.UP)) {
            entity.changeYPos(UP, speed);
            checkMoveAndProceed(Keys.UP, speed, entity);

            CURRENT_ROTATION = UP_ROTATION;
            playerRef.renderImage(isPacOpen, CURRENT_ROTATION);
        }
    }

    /**
     * Method to check if the move causes player to collide with a wall
     * This should be in the Wall class, an Overridden method of checkMoveCollision that would be in gameEntity class
     * */
    private boolean checkMoveWallCollision(gameEntity entity) {
        for (int i = NUM_GHOSTS + NUM_PLAYERS; i < NUM_GHOSTS + NUM_WALLS + NUM_PLAYERS; i++) { // where 1 == number of players: change this to a constant

            if (entity.intersects(gameEntities.get(i))) {

                return true;
            }
        }

        return false;
    }

    /**
     * Method to check if the move causes player to collide with a ghost
     * This should be in the Ghost class, an Overridden method of checkMoveCollision that would be in gameEntity class
     * Remember, inheritance saves you from repeating code: so if you have a LOT of repeated code, you might be
     * missing an inheritance relationship.
     * */
    private boolean checkMoveGhostCollision(gameEntity entity) {
        for (int i = NUM_PLAYERS; i <= NUM_GHOSTS; i++) {

            if (entity.intersects((gameEntities.get(i)))) {

                playerLives -= 1;
                return true;
            }
        }

        return false;
    }

    /**
     * Method to check if move causes player to collide with a dot
     * REPEATED CODE? PUT INTO A CLASS!!!
     * */
    private boolean checkMoveDotCollision(gameEntity entity) {

        for (int i = NUM_GHOSTS + NUM_WALLS + NUM_PLAYERS; i < totalGameEntities; i++) {

            if (entity.intersects(gameEntities.get(i))) {

                /* If collide with a dot, do not render that dot next frame: remove it from gameEntities */
                gameEntities.remove(gameEntities.get(i));
                totalGameEntities -= 1;
                return true;
            }
        }

        return false;
    }


    /**
     * Using checkMoveCollision methods, decides whether to proceed with a move or to undo it.
     * Should be a method in gameEntity
     * @param key key pressed
     * @param speed pixels moved per frame
     * @param entity entity that is moving
     */
    private void checkMoveAndProceed(Keys key, int speed, gameEntity entity) {

        if (checkMoveWallCollision(entity)) {

            /* Will collide with wall: need to undo the move */
            if (key == Keys.DOWN) {
                entity.changeYPos(UP, speed);
            }
            else if (key == Keys.UP) {
                entity.changeYPos(DOWN, speed);
            }
            else if (key == Keys.LEFT) {
                entity.changeXPos(RIGHT, speed);
            }
            else if (key == Keys.RIGHT) {
                entity.changeXPos(LEFT, speed);
            }
        }

        else if (checkMoveGhostCollision(entity)) {

            /* Reset entity's position. In this case, reset Player to starting position */
            entity.resetPosition();
            CURRENT_ROTATION = DEFAULT_ROTATION;

        }
        else if (checkMoveDotCollision(entity)) {

            playerScore += DOT_VALUE;
            scoreMessageString = SCORE_MESSAGE_STRING + playerScore;
            SCORE_MESSAGE.setMessage(scoreMessageString);
        }
    }

    /**
     * Method for checking if the game has been won yet.
     * Should probably be a method in Player class or gameEntity class - checks the player score or lives,
     * both of which would be (static) attributes of the Player.
     *  */
    private void winLoseDetection() {

        if (playerScore == WIN_SCORE) {

            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            WIN_MESSAGE.displayMessage();
        }
        else if (playerLives == NO_LIVES) {

            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            LOSE_MESSAGE.displayMessage();
        }
    }
}



