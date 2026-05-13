package bricker.main;

import bricker.brick_stratrgies.BasicCollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.Heart;
import bricker.gameobjects.UserPaddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import static danogl.collisions.Layer.STATIC_OBJECTS;

public class BrickerGameManager extends GameManager {
    private static final Vector2 WINDOW_DIMENSIONS = new Vector2(700, 500);
    private static final int BORDER_SIZE = 5;
    private static final int BALL_SPEED = 350;
    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final int DEFAULT_BRICK_COLS = 8;
    private static final String LOSE_MESSAGE = "You Lose! play again?";
    private static final String WIN_MESSAGE = "You Win! play again?";
    private static final int INITIAL_LIVES = 3;
    private static final int MAX_LIVES = 4;
    private static final float HEARTS_X_OFFSET = 30;
    private final int brickCols;
    private final int brickRows;
    private Vector2 windowDimensions;
    private Ball ball;
    private WindowController windowController;
    private int lives ;
    private GameObject[] hearts ;
    private TextRenderable livesText;
    private Counter brickCount ;
    private UserInputListener inputListener;

    /**
     * constructor with default brick values
     * @param windowTitle - name of the game
     * @param windowDimensions - size of the game window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.brickCols = DEFAULT_BRICK_COLS;
        this.brickRows = DEFAULT_BRICK_ROWS;
    }

    /**
     * constructor with number of breaks dependent on user input
     * @param windowTitle - name of the game
     * @param windowDimensions - size of the game window
     * @param brickCols - number of bricks per row
     * @param brickRows - number of brick rows
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int brickCols, int brickRows) {
        super(windowTitle, windowDimensions);

        this.brickCols = brickCols;
        this.brickRows = brickRows;
    }
    public static void main(String[] args) {
        BrickerGameManager brickerGameManager;
        if (args.length ==2){
            brickerGameManager= new BrickerGameManager(
                    "Bricker", WINDOW_DIMENSIONS,
                    Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        }
        else {
            brickerGameManager = new BrickerGameManager(
                    "Bricker", WINDOW_DIMENSIONS);
        }
        brickerGameManager.run();
    }

    /**
     * initializes all game objects and sets up the game world.
     * creates the ball, paddle, walls, bricks and background image.
     * @param imageReader - used to load all game images
     * @param soundReader - used to load the ball collision sound
     * @param inputListener - used to get keyboard input for the paddle
     * @param windowController - used to get the window dimensions
     */
    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader, UserInputListener inputListener,
            WindowController windowController) {
        this.windowController = windowController;
        this.inputListener = inputListener;
        super.initializeGame(imageReader, soundReader,  inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();

        // create ball
        createBall(imageReader, soundReader);

        // create paddle
        createPaddle(imageReader, inputListener);

        //  create walls
        makeGameWalls();
        // create brick
        brickCount = new Counter(0);
        createBricks(imageReader);
        // add background image
        makeBackground(imageReader);
        // create player lives
        hearts = new GameObject[MAX_LIVES];
        lives = INITIAL_LIVES;
        for (int i = 0; i < lives; i++) {
            addHeart(i, imageReader);
        }
        createHeartNumeric();
    }

    /**
     * a function that create a numeric representation of hearts
     */
    private void createHeartNumeric() {
        livesText = new TextRenderable(String.valueOf(lives));
        livesText.setColor(Color.green); // changes based on lives count
        GameObject livesCounter = new GameObject(
                new Vector2(10, windowDimensions.y() - 30),
                new Vector2(20, 20),
                livesText
        );
        gameObjects().addGameObject(livesCounter, Layer.UI);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkGameEnd();
    }

    /**
     * a function that checks if the game has ended by the ball passing the
     * paddle downwards or bricks becoming 0
     */
    private void checkGameEnd() {
        // use helper functions to check win or lose
        checkLoseCondition();
        checkWinCondition();
    }

    /**
     * a function that checks win condition
     */
    private void checkWinCondition() {
        if(brickCount.value()==0 ||inputListener.isKeyPressed(KeyEvent.VK_W)){
            handleEndDialog(WIN_MESSAGE);
        }
    }

    /**
     * a function to handle the dialog printed on screen
     * @param message - message to print win/lose
     */
    private void handleEndDialog(String message) {
        if (windowController.openYesNoDialog(message)) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    /**
     * a function to check if the game is lost
     */
    private void checkLoseCondition() {
        if (ball.getCenter().y() > windowDimensions.y()){
            lives--;
            removeHeart(lives);
            updateLivesNumeric();
            if(lives==0) {
                handleEndDialog(LOSE_MESSAGE);
            }
            else {
                // respawn ball
                spawnBall();
            }
        }
    }

    private void updateLivesNumeric() {
        if (lives >= 3) livesText.setColor(Color.green);
        else if (lives == 2) livesText.setColor(Color.yellow);
        else livesText.setColor(Color.red);
        livesText.setString(String.valueOf(lives));
    }

    /**
     * a function to add a heart in the game
     * cannot make more than 4 hearts i.e. index 3
     * @param index - the index of the heart
     * @param imageReader - used to load the heart image
     */
    private void addHeart(int index,ImageReader imageReader) {
        if (index >=MAX_LIVES) return;
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        GameObject heart = new Heart(
                new Vector2(HEARTS_X_OFFSET + index * 30, windowDimensions.y() - 30),
                new Vector2(20, 20),
                heartImage
        );
        gameObjects().addGameObject(heart, Layer.UI);
        hearts[index] =heart;
    }

    /**
     * a function that removes a heart from the game
     */
    private void removeHeart(int index) {
        GameObject heart = hearts[index];
        gameObjects().removeGameObject(heart, Layer.UI);
    }

    /**
     * a function that creates a background for the game
     * @param imageReader -used to load the background image
     */
    private void makeBackground(ImageReader imageReader) {
        Renderable backgroundImage =
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background =
                new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()), backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * a function that creates a brick, we decided to make strategy in charge of
     * making the brick disappear because in each strategy something is going to happens
     * when it disappears so it make sense to give the responsibility to the strategy
     * @param imageReader - used to load the brick image
     */
    private void createBricks(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        float brickWidth = (windowDimensions.x() - 10) / brickCols; // 10 for the two walls
        float brickHeight = 15;
        for (int row = 0; row < brickRows; row++) {
            for (int col = 0; col < brickCols; col++) {
                Vector2 position = new Vector2( 5 + col *brickWidth,
                        5 + row * (brickHeight));
                GameObject brick =
                        new Brick(position,
                                new Vector2(brickWidth, 15),
                                brickImage,1,1, new BasicCollisionStrategy(gameObjects(),brickCount)
                        );
                brickCount.increment();
                gameObjects().addGameObject(brick,STATIC_OBJECTS);
            }
        }
    }

    /**
     * creates the user paddle and places it at the bottom center of the screen
     * @param imageReader - used to load the paddle image
     * @param inputListener - listens for keyboard input to move the paddle

     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject userPaddle =
                new UserPaddle(Vector2.ZERO,
                        new Vector2(100, 15),
                        paddleImage,
                        inputListener);
        userPaddle.setCenter(
                new Vector2(windowDimensions.x()/2, windowDimensions.y()-30));
        gameObjects().addGameObject(userPaddle);
    }

    /**
     * creates the ball in the center of the screen with a sound for collisions
     * @param imageReader - used to load the ball image
     * @param soundReader - used to load the collision sound
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage =
                imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        ball =
                new Ball(new Vector2(0,0), new Vector2(20, 20), ballImage, collisionSound);
        spawnBall();
        this.gameObjects().addGameObject(ball);
    }

    /**
     * a function that spawn the ball, used as a helper function because of use
     * with multiple lives
     */
    private void spawnBall() {
        ball.setCenter(new Vector2(windowDimensions.x()/2 , windowDimensions.y()-60 ));

        int ballVelY = BALL_SPEED;
        int ballVelX = BALL_SPEED;
        Random random = new Random();
        if (random.nextBoolean()){
            ballVelX*=-1;
        }
        if (random.nextBoolean()){
            ballVelY*=-1;
        }
        ball.setVelocity(new Vector2(ballVelX,ballVelY));
    }

    /**
     * a method to create 3 walls for the game ( to make sure the ball won't
     * go out of bounds)
     */
    private void makeGameWalls() {
        GameObject leftWall =
                new GameObject(Vector2.ZERO, new Vector2( BORDER_SIZE,WINDOW_DIMENSIONS.y()), null);
        this.gameObjects().addGameObject(leftWall);

        GameObject rightWall =
                new GameObject(new Vector2(WINDOW_DIMENSIONS.x()- BORDER_SIZE, 0) , new Vector2( BORDER_SIZE,WINDOW_DIMENSIONS.y()), null);
        this.gameObjects().addGameObject(rightWall);

        GameObject upperWall =
                new GameObject(Vector2.ZERO , new Vector2(WINDOW_DIMENSIONS.x(),  BORDER_SIZE), null);
        this.gameObjects().addGameObject(upperWall);
    }


}
