package com.sideprojects.megamanxphantomblade.renderers.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by buivuhoang on 19/02/17.
 */
public class DamagedShader {
    public static ShaderProgram getShader() {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying LOWP vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  vec4 color = texture2D(u_texture, v_texCoords) * v_color;\n" //
                + "  color.r = 1.;\n" //
                + "  color.g = 1.;\n" //
                + "  color.b = 1.;\n" //
//                + "  color.b = 1. - color.b;\n" //
                + "  color.rgb = 2. * color.rgb;\n" //
                + "  gl_FragColor = color;\n" //
                + "}\n";
        // TODO: implement correct shader to map dark to white, rest to blue
        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        return shader;
    }
}
