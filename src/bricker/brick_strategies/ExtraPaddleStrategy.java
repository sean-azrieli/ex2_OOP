package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.ExtraPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddleStrategy extends CollisionStrategyDecorator {
    private static final String PADDLE_IMAGE = "assets/paddle.png";

    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Vector2 paddleDimensions;
    private final Vector2 windowDimensions;
    private final Counter extraPaddleCount;

    public ExtraPaddleStrategy(CollisionStrategy wrapped, GameObjectCollection gameObjects,
                               ImageReader imageReader, UserInputListener inputListener,
                               Vector2 paddleDimensions, Vector2 windowDimensions) {
        super(wrapped);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.paddleDimensions = paddleDimensions;
        this.windowDimensions = windowDimensions;
        this.extraPaddleCount = new Counter(0);
    }

    @Override
    public void onCollision(Brick thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if (extraPaddleCount.value() == 0) {
            extraPaddleCount.increment();
            createExtraPaddle();
        }
    }

    private void createExtraPaddle() {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE, true);
        ExtraPaddle extraPaddle = new ExtraPaddle(
                Vector2.ZERO, paddleDimensions, paddleImage,
                inputListener, gameObjects, extraPaddleCount
        );
        extraPaddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2));
        gameObjects.addGameObject(extraPaddle);
    }
}
