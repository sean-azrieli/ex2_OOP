/**
 * a class to implement the basic collision strategy
 */
package bricker.brick_stratrgies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

import static danogl.collisions.Layer.STATIC_OBJECTS;

public class BasicCollisionStrategy implements CollisionStrategy{

    private final GameObjectCollection gameObjects;
    private final Counter brickCount;

    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter brickCount){
        this.gameObjects = gameObjects;
        this.brickCount = brickCount;
    }

    @Override
    public void onCollision(Brick brick, GameObject ball) {
        gameObjects.removeGameObject(brick,STATIC_OBJECTS);
        brickCount.decrement();
    }
}
