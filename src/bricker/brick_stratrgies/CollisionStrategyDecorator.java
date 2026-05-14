package bricker.brick_stratrgies;

import danogl.GameObject;

public abstract class CollisionStrategyDecorator implements CollisionStrategy {
    private final CollisionStrategy wrapped;

    public CollisionStrategyDecorator(CollisionStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        wrapped.onCollision( thisObj, otherObj); // always call wrapped first
    }
}
