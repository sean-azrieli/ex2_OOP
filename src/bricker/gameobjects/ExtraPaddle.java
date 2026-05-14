package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddle extends UserPaddle {
    private static final int MAX_HITS = 4;

    private final GameObjectCollection gameObjects;
    private final Counter extraPaddleCount;
    private int hitCount;

    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, GameObjectCollection gameObjects,
                       Counter extraPaddleCount) {
        super(topLeftCorner, dimensions, renderable, inputListener);
        this.gameObjects = gameObjects;
        this.extraPaddleCount = extraPaddleCount;
        this.hitCount = 0;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        hitCount++;
        if (hitCount >= MAX_HITS) {
            // prevent double-removal
            if (gameObjects.removeGameObject(this)) {
                extraPaddleCount.decrement();
            }
        }
    }
}
