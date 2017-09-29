package com.sideprojects.megamanxphantomblade.renderers.shaders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by buivuhoang on 28/09/17.
 */
public abstract class Shader implements Disposable {
    protected ShaderProgram shaderProgram;

    protected Shader(ShaderProgram shader) {
        this.shaderProgram = shader;
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
