package com.sideprojects.megamanxphantomblade.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.sideprojects.megamanxphantomblade.animation.AnimationLoader;
import com.sideprojects.megamanxphantomblade.animation.Sprites;

/**
 * Created by buivuhoang on 25/03/17.
 */
public class PlayerHealthRenderer implements Disposable {
    private ShapeRenderer healthRenderer;
    private SpriteBatch batch;
    private Array<TextureAtlas.AtlasRegion> guiElements;

    // Color of health
    private static Color normal = Color.GREEN;
    private static Color lowHealth = Color.YELLOW;
    private static Color background = Color.RED;

    // Position of health bar on screen
    private static int healthBarPosX = 20;
    private static int healthBarPosY = 20;
    private static int healthPosX = 29;
    private static int healthPosY = 20;
    private static int healthBarUnitLength = 2;
    private static int healthWidth = 5;
    private static int healthPointsPerUnit = 5;

    // Index of health bar texture
    private static int XHealthBarIndex = 3;

    // Variables used for rendering health bar
    private int maxHealth = -1;
    private int numberOfSectionsMax;

    // Red background that slowly reclines to current health when damaged
    private int backgroundDescendingHealth;
    private float descendWaitPeriod = 0.4f;
    private float descendingRate = 0.05f;
    private float stateTime;
    private boolean startDescending;

    PlayerHealthRenderer(SpriteBatch batch) {
        this.batch = batch;
        healthRenderer = new ShapeRenderer();
        healthRenderer.setAutoShapeType(true);
        guiElements = AnimationLoader.loadGui(Sprites.GUI_ELEMENTS);
        stateTime = 0;
        startDescending = false;
    }

    void renderHealthContanier(int maxHealth) {
        TextureRegion top = guiElements.get(XHealthBarIndex);
        TextureRegion middle = guiElements.get(XHealthBarIndex + 1);
        TextureRegion bottom = guiElements.get(XHealthBarIndex + 2);
        int startPos = healthBarPosY;
        batch.draw(top, healthBarPosX, startPos);

        if (this.maxHealth != maxHealth) {
            this.maxHealth = maxHealth;
            numberOfSectionsMax = convertToSections(maxHealth);
            backgroundDescendingHealth = maxHealth;
        }

        // Render heath bar container
        for (int i = 0; i < numberOfSectionsMax - 1; i++) {
            startPos += healthBarUnitLength;
            batch.draw(middle, healthBarPosX, startPos);
        }
        startPos -= 4;
        batch.draw(bottom, healthBarPosX, startPos);
    }

    void renderHealth(int maxHealth, int currentHealth, boolean isLowHealth, float delta) {
        // Set colour
        healthRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        healthRenderer.begin();
        healthRenderer.set(ShapeRenderer.ShapeType.Filled);
        if (isLowHealth) {
            healthRenderer.setColor(lowHealth);
        } else {
            healthRenderer.setColor(normal);
        }

        // Render health
        int startPosForHealth = healthPosY + healthBarUnitLength * convertToSections(maxHealth);
        int numberOfSections = convertToSections(currentHealth);
        for (int i = 0; i < numberOfSections; i++) {
            healthRenderer.rect(healthPosX, startPosForHealth, healthWidth, healthBarUnitLength);
            startPosForHealth -= healthBarUnitLength;
        }

        // Render red descending background
        // Set timing to render red descending background
        float backgroundHeight = (backgroundDescendingHealth - currentHealth) * healthBarUnitLength / (float)healthPointsPerUnit;
        if (backgroundHeight <= 0) {
            stateTime = 0;
            startDescending = false;
        } else {
            stateTime += delta;
        }
        if (stateTime >= descendWaitPeriod) {
            startDescending = true;
        }
        if (startDescending && stateTime >= descendingRate &&
                backgroundDescendingHealth > currentHealth) {
            stateTime = 0;
            backgroundDescendingHealth -= 1;
        }
        startPosForHealth -= backgroundHeight - healthBarUnitLength;
        if (backgroundHeight > 0) {
            healthRenderer.setColor(background);
            healthRenderer.rect(healthPosX, startPosForHealth, healthWidth, backgroundHeight);
        }

        healthRenderer.end();
    }

    private int convertToSections(int health) {
        return (int)Math.ceil(health / (double)healthPointsPerUnit);
    }

    @Override
    public void dispose() {
        healthRenderer.dispose();
    }
}
