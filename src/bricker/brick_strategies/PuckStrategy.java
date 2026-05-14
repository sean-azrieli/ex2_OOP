package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class PuckStrategy extends CollisionStrategyDecorator {
    private static final String PUCK_IMAGE = "assets/mockBall.png";
    private static final String PUCK_SOUND = "assets/blop.wav";
    private static final int NUM_PUCKS = 2;

    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Vector2 puckDimensions;
    private final float ballSpeed;
    private final float windowHeight;

    public PuckStrategy(CollisionStrategy wrapped, GameObjectCollection gameObjects,
                        ImageReader imageReader, SoundReader soundReader,
                        Vector2 ballDimensions, float ballSpeed, float windowHeight) {
        super(wrapped);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.puckDimensions = ballDimensions.mult(3f / 4f);
        this.ballSpeed = ballSpeed;
        this.windowHeight = windowHeight;
    }

    @Override
    public void onCollision(Brick thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        Vector2 brickCenter = thisObj.getCenter();
        for (int i = 0; i < NUM_PUCKS; i++) {
            createPuck(brickCenter);
        }
    }

    private void createPuck(Vector2 location) {
        Renderable puckImage = imageReader.readImage(PUCK_IMAGE, true);
        Sound collisionSound = soundReader.readSound(PUCK_SOUND);
        Puck puck = new Puck(location, puckDimensions, puckImage, collisionSound,
                gameObjects, windowHeight);
        puck.setVelocity(randomUpperHalfVelocity());
        gameObjects.addGameObject(puck);
    }

    private Vector2 randomUpperHalfVelocity() {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * ballSpeed;
        float velocityY = (float) Math.sin(angle) * ballSpeed;
        return new Vector2(velocityX, velocityY);
    }
}
