package com.sideprojects.megamanxphantomblade.renderers.shaders;

/**
 * Created by buivuhoang on 19/02/17.
 */
public class DamagedShader extends Shader {
    public DamagedShader() {
        super(ShaderProgramFactory.createFromFile(
                Shaders.VERT_PASSTHROUGH,
                Shaders.FRAG_BRIGHTEN));
    }

    @Override
    protected void setGpuVariables() {
        shader.setUniformf("intensity", 0.5f);
    }
}
