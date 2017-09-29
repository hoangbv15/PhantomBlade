package com.sideprojects.megamanxphantomblade.renderers.shaders;

import com.sideprojects.megamanxphantomblade.logging.Logger;
import com.sideprojects.megamanxphantomblade.player.TraceColour;

/**
 * Created by buivuhoang on 19/02/17.
 */
public class TraceShader extends Shader {
    private final TraceColour colour;
    public TraceShader(Logger logger, TraceColour colour) {
        super(logger, Shaders.VERT_COLOUR,
                Shaders.FRAG_PASSTHROUGH
        );
        this.colour = colour;
    }

    @Override
    public void setGpuVariables() {
        shaderProgram.setUniformf("custom_color", colour.r, colour.g, colour.b, 0.3f);
    }
}
