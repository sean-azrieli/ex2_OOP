package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

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
    private static final Vector2 HEART_DIMENSIONS = new Vector2(20, 20);
    private static final Vector2 BALL_DIMENSIONS = new Vector2(20, 20);

    private final int brickCols;
    private final int brickRows;
    private Vector2 windowDimensions;
    private Ball ball;
    private UserPaddle userPaddle;
    private WindowController windowController;
    private Counter livesCounter;
    private Counter brickCount;
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
     * constructor with number of bricks dependent on user input
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
        if (args.length == 2) {
            brickerGameManager = new BrickerGameManager(
                    "Bricker", WINDOW_DIMENSIONS,
                    Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } else {
            brickerGameManager = new BrickerGameManager("Bricker", WINDOW_DIMENSIONS);
        }
        brickerGameManager.run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.windowController = windowController;
        this.inputListener = inputListener;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();

        createBall(imageReader, soundReader);
        userPaddle = createPaddle(imageReader, inputListener);
        makeGameWalls();

        brickCount = new Counter(0);
        createBricks(imageReader);
        makeBackground(imageReader);

        livesCounter = new Counter(INITIAL_LIVES);
        createLifeCounters(imageReader);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkGameEnd();
    }

    private void checkGameEnd() {
        checkLoseCondition();
        checkWinCondition();
    }

    private void checkWinCondition() {
        if (brickCount.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            handleEndDialog(WIN_MESSAGE);
        }
    }

    private void handleEndDialog(String message) {
        if (windowController.openYesNoDialog(message)) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    private void checkLoseCondition() {
        if (ball.getCenter().y() > windowDimensions.y()) {
            livesCounter.decrement();
            if (livesCounter.value() == 0) {
                handleEndDialog(LOSE_MESSAGE);
            } else {
                spawnBall();
            }
        }
    }

    private void createLifeCounters(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        Vector2 heartsTopLeft = new Vector2(HEARTS_X_OFFSET, windowDimensions.y() - 30);

        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(
                heartsTopLeft, livesCounter, heartImage, HEART_DIMENSIONS,
                gameObjects(), MAX_LIVES
        );
        gameObjects().addGameObject(graphicLifeCounter, Layer.UI);

        TextRenderable livesText = new TextRenderable(String.valueOf(INITIAL_LIVES));
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(
                new Vector2(10, windowDimensions.y() - 30),
                new Vector2(20, 20),
                livesText, livesCounter
        );
        gameObjects().addGameObject(numericLifeCounter, Layer.UI);
    }

    private void makeBackground(ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createBricks(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        float brickWidth = (windowDimensions.x() - 10) / brickCols;
        float brickHeight = 15;
        for (int row = 0; row < brickRows; row++) {
            for (int col = 0; col < brickCols; col++) {
                Vector2 position = new Vector2(5 + col * brickWidth, 5 + row * brickHeight);
                Brick brick = new Brick(position, new Vector2(brickWidth, brickHeight),
                        brickImage, row, col, new BasicCollisionStrategy(gameObjects(), brickCount));
                brickCount.increment();
                gameObjects().addGameObject(brick, STATIC_OBJECTS);
            }
        }
    }

    private UserPaddle createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        UserPaddle paddle = new UserPaddle(Vector2.ZERO, new Vector2(100, 15),
                paddleImage, inputListener);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);
        return paddle;
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        ball = new Ball(Vector2.ZERO, BALL_DIMENSIONS, ballImage, collisionSound);
        spawnBall();
        gameObjects().addGameObject(ball);
    }

    private void spawnBall() {
        ball.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 60));
        Random random = new Random();
        int ballVelX = random.nextBoolean() ? BALL_SPEED : -BALL_SPEED;
        int ballVelY = random.nextBoolean() ? BALL_SPEED : -BALL_SPEED;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    private void makeGameWalls() {
        gameObjects().addGameObject(new GameObject(
                Vector2.ZERO, new Vector2(BORDER_SIZE, WINDOW_DIMENSIONS.y()), null));
        gameObjects().addGameObject(new GameObject(
                new Vector2(WINDOW_DIMENSIONS.x() - BORDER_SIZE, 0),
                new Vector2(BORDER_SIZE, WINDOW_DIMENSIONS.y()), null));
        gameObjects().addGameObject(new GameObject(
                Vector2.ZERO, new Vector2(WINDOW_DIMENSIONS.x(), BORDER_SIZE), null));
    }
}
