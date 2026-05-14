package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {
    private static final float HEART_SPACING = 30;

    private final Counter livesCounter;
    private final GameObjectCollection gameObjects;
    private final Renderable heartImage;
    private final Vector2 heartDimensions;
    private final int maxLives;
    private final GameObject[] hearts;
    private int displayedLives;

    public GraphicLifeCounter(Vector2 topLeftCorner, Counter livesCounter,
                               Renderable heartImage, Vector2 heartDimensions,
                               GameObjectCollection gameObjects, int maxLives) {
        super(topLeftCorner, Vector2.ZERO, null);
        this.livesCounter = livesCounter;
        this.gameObjects = gameObjects;
        this.heartImage = heartImage;
        this.heartDimensions = heartDimensions;
        this.maxLives = maxLives;
        this.hearts = new GameObject[maxLives];
        this.displayedLives = livesCounter.value();
        for (int i = 0; i < displayedLives; i++) {
            addHeart(i);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int currentLives = livesCounter.value();
        while (displayedLives < currentLives && displayedLives < maxLives) {
            addHeart(displayedLives);
            displayedLives++;
        }
        while (displayedLives > currentLives && displayedLives > 0) {
            displayedLives--;
            removeHeart(displayedLives);
        }
    }

    private void addHeart(int index) {
        Vector2 position = new Vector2(
                getTopLeftCorner().x() + index * HEART_SPACING,
                getTopLeftCorner().y());
        hearts[index] = new Heart(position, heartDimensions, heartImage);
        gameObjects.addGameObject(hearts[index], Layer.UI);
    }

    private void removeHeart(int index) {
        gameObjects.removeGameObject(hearts[index], Layer.UI);
        hearts[index] = null;
    }
}
