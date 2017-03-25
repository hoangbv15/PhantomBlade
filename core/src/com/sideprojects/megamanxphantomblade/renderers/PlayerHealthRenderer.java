package com.sideprojects.megamanxphantomblade.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.sideprojects.megamanxphantomblade.animation.AnimationLoader;
import com.sideprojects.megamanxphantomblade.animation.Sprites;

/**
 * Created by buivuhoang on 25/03/17.
 */
public class PlayerHealthRenderer {
    private ShapeRenderer healthRenderer;
    private SpriteBatch batch;
    private Array<TextureAtlas.AtlasRegion> guiElements;

    // Color of health
    private static Color normal = Color.GREEN;
    private static Color lowHealth = Color.YELLOW;

    // Position of health bar on screen
    private static int healthBarPosX = 20;
    private static int healthBarPosY = 20;
    private static int healthPosX = 29;
    private static int healthPosY = 19;
    private static int healthBarUnitLength = 4;
    private static int healthWidth = 5;

    // Index of health bar texture
    private static int XHealthBarIndex = 3;

    // Variables used for rendering health bar
    private int maxHealth = -1;
    private int numberOfSectionsMax;

    public PlayerHealthRenderer(SpriteBatch batch) {
        this.batch = batch;
        healthRenderer = new ShapeRenderer();
        healthRenderer.setAutoShapeType(true);
        guiElements = AnimationLoader.loadGui(Sprites.GuiElements);
    }

    public void renderHealthContanier(int maxHealth) {
        TextureRegion top = guiElements.get(XHealthBarIndex);
        TextureRegion middle = guiElements.get(XHealthBarIndex + 1);
        TextureRegion bottom = guiElements.get(XHealthBarIndex + 2);
        int startPos = healthBarPosY;
        batch.draw(top, healthBarPosX, startPos);

        if (this.maxHealth == -1) {
            this.maxHealth = maxHealth;
            numberOfSectionsMax = convertToSections(maxHealth);
        }

        // Render heath bar container
        for (int i = 0; i < numberOfSectionsMax - 1; i++) {
            startPos += healthBarUnitLength;
            batch.draw(middle, healthBarPosX, startPos);
        }

        batch.draw(bottom, healthBarPosX, startPos);
    }

    public void renderHealth(int maxHealth, int currentHealth, boolean isLowHealth, float delta) {
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

        healthRenderer.end();
    }

    private int convertToSections(int health) {
        return (int)Math.ceil(health / 5);
    }

    public void dispose() {
        healthRenderer.dispose();
    }
}
