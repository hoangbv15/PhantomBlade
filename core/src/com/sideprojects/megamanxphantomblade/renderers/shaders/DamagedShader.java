package com.sideprojects.megamanxphantomblade.renderers.shaders;

import com.sideprojects.megamanxphantomblade.logging.Logger;

/**
 * Created by buivuhoang on 19/02/17.
 */
public class DamagedShader extends Shader {
    public DamagedShader(Logger logger) {
        super(logger, Shaders.VERT_PASSTHROUGH,
                Shaders.FRAG_BRIGHTEN);
    }

    @Override
    protected void setGpuVariables() {
        shaderProgram.setUniformf("intensity", 0.5f);
    }
}
