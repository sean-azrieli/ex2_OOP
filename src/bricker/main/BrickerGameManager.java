package bricker.main;

import bricker.gameobjects.Ball;
import bricker.gameobjects.UserPaddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import static javax.swing.text.html.CSS.Attribute.BORDER_COLOR;

public class BrickerGameManager extends GameManager {

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }
    public static void main(String[] args) {
        BrickerGameManager brickerGameManager = new BrickerGameManager(
                "Bricker", new Vector2(700,500));
        brickerGameManager.run();
    }

    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader, UserInputListener inputListener,
            WindowController windowController) {
        super.initializeGame(imageReader, soundReader,  inputListener, windowController);

        // create ball
        Renderable ballImage =
                imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        GameObject ball =
                new Ball(new Vector2(0,0), new Vector2(50, 50), ballImage, collisionSound);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(0.5f));
        ball.setVelocity(Vector2.DOWN.mult(300));
        this.gameObjects().addGameObject(ball);

        // create paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject userPaddle =
                new UserPaddle(Vector2.ZERO,
                        new Vector2(100, 15),
                        paddleImage,
                        inputListener);
        userPaddle.setCenter(
                new Vector2(windowDimensions.x()/2, windowDimensions.y()-30));
        gameObjects().addGameObject(userPaddle);

        //  create walls
        GameObject leftWall =
                new GameObject(Vector2.ZERO, new Vector2(5, 500), null);
        this.gameObjects().addGameObject(leftWall);

        GameObject rightWall =
                new GameObject(new Vector2(695, 0) , new Vector2(5, 500), null);
        this.gameObjects().addGameObject(rightWall);

        GameObject upperWall =
                new GameObject(Vector2.ZERO , new Vector2(700, 5), null);
        this.gameObjects().addGameObject(upperWall);

        GameObject bottomWall =
                new GameObject(new Vector2(0, 495) , new Vector2(700, 5), null);
        this.gameObjects().addGameObject(bottomWall);

        // add background image
        Renderable backgroundImage =
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background =
                new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()), backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);


    }


}
