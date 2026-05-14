package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

import static danogl.collisions.Layer.STATIC_OBJECTS;

public class BasicCollisionStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjects;
    private final Counter brickCount;

    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter brickCount) {
        this.gameObjects = gameObjects;
        this.brickCount = brickCount;
    }

    @Override
    public void onCollision(Brick thisObj, GameObject otherObj) {
        if (gameObjects.removeGameObject(thisObj, STATIC_OBJECTS)) {
            brickCount.decrement();
        }
    }
}
