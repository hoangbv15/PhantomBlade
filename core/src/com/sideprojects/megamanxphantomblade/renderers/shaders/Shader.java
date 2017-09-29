package com.sideprojects.megamanxphantomblade.renderers.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.sideprojects.megamanxphantomblade.logging.Logger;

/**
 * Created by buivuhoang on 28/09/17.
 */
public abstract class Shader implements Disposable {
    protected ShaderProgram shaderProgram;

    protected Shader(Logger logger, String vertFileName, String fragFileName) {
        shaderProgram = new ShaderProgram(
                Gdx.files.internal(Shaders.VERT_SHADERS_PATH + vertFileName),
                Gdx.files.internal(Shaders.FRAG_SHADERS_PATH + fragFileName));

        if (!shaderProgram.isCompiled()) {
            logger.error(shaderProgram.getLog());
        } else if (shaderProgram.getLog().length() != 0 && logger.isInfoEnabled()){
            logger.info(shaderProgram.getLog());
        }
    }

    public void apply(SpriteBatch batch) {
        batch.setShader(shaderProgram);
        setGpuVariables();
    }

    protected void setGpuVariables() {}

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }
}
