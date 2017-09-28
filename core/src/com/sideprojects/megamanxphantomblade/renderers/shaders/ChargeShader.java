package com.sideprojects.megamanxphantomblade.renderers.shaders;

/**
 * Created by buivuhoang on 19/02/17.
 */
public class ChargeShader extends Shader {
    public ChargeShader() {
        super(ShaderProgramFactory.createFromFile(
                Shaders.VERT_PASSTHROUGH,
                Shaders.FRAG_BRIGHTEN));
    }

    @Override
    protected void setGpuVariables() {
        shader.setUniformf("intensity", 0.2f);
    }
}
