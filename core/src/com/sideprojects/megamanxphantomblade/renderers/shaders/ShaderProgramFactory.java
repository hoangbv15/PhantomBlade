package com.sideprojects.megamanxphantomblade.renderers.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by buivuhoang on 27/09/17.
 */
class ShaderProgramFactory {
    protected static ShaderProgram createFromFile(String vertFileName, String fragFileName) {
        ShaderProgram shader = new ShaderProgram(
                Gdx.files.internal(Shaders.VERT_SHADERS_PATH + vertFileName),
                Gdx.files.internal(Shaders.FRAG_SHADERS_PATH + fragFileName));

        if (!shader.isCompiled()) {
            System.err.println(shader.getLog());
            System.exit(0);
        }

        if (shader.getLog().length()!=0){
            System.out.println(shader.getLog());
        }
        return shader;
    }
}
