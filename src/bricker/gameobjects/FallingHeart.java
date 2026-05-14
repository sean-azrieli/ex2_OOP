package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class FallingHeart extends GameObject {
    private static final float FALL_SPEED = 100;

    private final GameObjectCollection gameObjects;
    private final GameObject originalPaddle;
    private final Counter livesCounter;
    private final int maxLives;
    private final float windowHeight;

    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        GameObjectCollection gameObjects, GameObject originalPaddle,
                        Counter livesCounter, int maxLives, float windowHeight) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.originalPaddle = originalPaddle;
        this.livesCounter = livesCounter;
        this.maxLives = maxLives;
        this.windowHeight = windowHeight;
        setVelocity(new Vector2(0, FALL_SPEED));
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {

        return other == originalPaddle;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (livesCounter.value() < maxLives) {
            livesCounter.increment();
        }
        gameObjects.removeGameObject(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getCenter().y() > windowHeight) {
            gameObjects.removeGameObject(this);
        }
    }
}
