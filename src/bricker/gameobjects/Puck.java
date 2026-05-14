package bricker.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Puck extends Ball {
    private final GameObjectCollection gameObjects;
    private final float windowHeight;

    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, GameObjectCollection gameObjects, float windowHeight) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.gameObjects = gameObjects;
        this.windowHeight = windowHeight;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getCenter().y() > windowHeight) {
            gameObjects.removeGameObject(this);
        }
    }
}
