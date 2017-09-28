package com.sideprojects.megamanxphantomblade.renderers.shaders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by buivuhoang on 28/09/17.
 */
public abstract class Shader implements Disposable {
    protected ShaderProgram shader;

    protected Shader(ShaderProgram shader) {
        this.shader = shader;
    }

    public void apply(SpriteBatch batch) {
        batch.setShader(shader);
        setGpuVariables();
    }

    protected void setGpuVariables() {}

    @Override
    public void dispose() {
        shader.dispose();
    }
}
