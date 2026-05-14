package bricker.brick_stratrgies;

import bricker.gameobjects.Brick;
import danogl.GameObject;

public interface CollisionStrategy {

    void onCollision(GameObject thisObj, GameObject otherObj);
}
