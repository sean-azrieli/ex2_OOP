package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.FallingHeart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class LifeStrategy extends CollisionStrategyDecorator {
    private final GameObjectCollection gameObjects;
    private final Renderable heartRenderable;
    private final Vector2 heartDimensions;
    private final GameObject originalPaddle;
    private final Counter livesCounter;
    private final int maxLives;
    private final float windowHeight;

    public LifeStrategy(CollisionStrategy wrapped, GameObjectCollection gameObjects,
                        Renderable heartRenderable, Vector2 heartDimensions,
                        GameObject originalPaddle, Counter livesCounter,
                        int maxLives, float windowHeight) {
        super(wrapped);
        this.gameObjects = gameObjects;
        this.heartRenderable = heartRenderable;
        this.heartDimensions = heartDimensions;
        this.originalPaddle = originalPaddle;
        this.livesCounter = livesCounter;
        this.maxLives = maxLives;
        this.windowHeight = windowHeight;
    }

    @Override
    public void onCollision(Brick thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        FallingHeart heart = new FallingHeart(
                thisObj.getCenter(), heartDimensions, heartRenderable,
                gameObjects, originalPaddle, livesCounter, maxLives, windowHeight
        );
        gameObjects.addGameObject(heart);
    }
}
