package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {
    private final Counter livesCounter;
    private final TextRenderable textRenderable;
    private int displayedLives;

    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions,
                               TextRenderable textRenderable, Counter livesCounter) {
        super(topLeftCorner, dimensions, textRenderable);
        this.livesCounter = livesCounter;
        this.textRenderable = textRenderable;
        this.displayedLives = livesCounter.value();
        updateColor(displayedLives);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int currentLives = livesCounter.value();
        if (currentLives != displayedLives) {
            displayedLives = currentLives;
            textRenderable.setString(String.valueOf(currentLives));
            updateColor(currentLives);
        }
    }

    private void updateColor(int lives) {
        if (lives >= 3) textRenderable.setColor(Color.GREEN);
        else if (lives == 2) textRenderable.setColor(Color.YELLOW);
        else textRenderable.setColor(Color.RED);
    }
}
