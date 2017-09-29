package com.sideprojects.megamanxphantomblade.renderers.shaders;

import com.sideprojects.megamanxphantomblade.logging.Logger;

/**
 * Created by buivuhoang on 19/02/17.
 */
public class ChargeShader extends Shader {
    public ChargeShader(Logger logger) {
        super(logger, Shaders.VERT_PASSTHROUGH,
                Shaders.FRAG_BRIGHTEN);
    }

    @Override
    protected void setGpuVariables() {
        shaderProgram.setUniformf("intensity", 0.2f);
    }
}
