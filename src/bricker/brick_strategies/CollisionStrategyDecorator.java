package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;

public abstract class CollisionStrategyDecorator implements CollisionStrategy {
    private final CollisionStrategy wrapped;

    public CollisionStrategyDecorator(CollisionStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void onCollision(Brick thisObj, GameObject otherObj) {
        wrapped.onCollision(thisObj, otherObj);
    }
}
