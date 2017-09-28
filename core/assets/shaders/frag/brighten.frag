#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float intensity;
void main()
{
    vec4 f_color = texture2D(u_texture, v_texCoords);
    f_color.rgb /= f_color.a;
    f_color.rgb += intensity;
    f_color.rgb *= f_color.a;
    gl_FragColor = f_color;
}