package com.sideprojects.megamanxphantomblade.renderers.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.logging.Logger;

/**
 * Created by buivuhoang on 30/09/17.
 */
public class PaletteSwapShader extends Shader {
    private final float paletteIndex;
    private final Texture colorTable;

    public PaletteSwapShader(Logger logger) {
        super(logger, Shaders.VERT_PASSTHROUGH,
                Shaders.FRAG_PALETTESWAP);
        colorTable = new Texture(Sprites.X_PALETTES);
        float currentPalette = 1; //A number from 0 to (colorTable.getHeight() - 1)
        paletteIndex = (currentPalette + 0.5f) / colorTable.getHeight();
    }

    @Override
    protected void setGpuVariables() {
        colorTable.bind(1);

        //Must return active texture unit to default of 0 before batch.end
        //because SpriteBatch does not automatically do this. It will bind the
        //sprite's texture to whatever the current active unit is, and assumes
        //the active unit is 0
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        shaderProgram.setUniformi("colorTable", 1);
        shaderProgram.setUniformf("paletteIndex", paletteIndex);
    }
}
