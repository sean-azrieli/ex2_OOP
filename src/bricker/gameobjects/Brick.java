package bricker.gameobjects;

import bricker.brick_stratrgies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Brick extends GameObject {
    private final CollisionStrategy strategy;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, int row, int col, CollisionStrategy strategy) {
        super(topLeftCorner, dimensions, renderable);
        this.strategy = strategy;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        strategy.onCollision(this,other);
    }
}
