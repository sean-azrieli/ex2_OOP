package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Brick extends GameObject {
    private final CollisionStrategy strategy;
    private final int row;
    private final int col;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 int row, int col, CollisionStrategy strategy) {
        super(topLeftCorner, dimensions, renderable);
        this.row = row;
        this.col = col;
        this.strategy = strategy;
    }

    public int getRow() { return row; }

    public int getCol() { return col; }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        strategy.onCollision(this, other);
    }
}
