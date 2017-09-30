// Thanks to Tenfour04 http://stackoverflow.com/questions/26132160/simulating-palette-swaps-with-opengl-shaders-in-libgdx/26141687#26141687
uniform sampler2D u_texture; // Texture to which we'll apply our shader? (should its name be u_texture?)
varying vec2 v_texCoords;

// User uniforms
uniform sampler2D colorTable; // Color table with 6x6 pixels (6 palettes of 6 colors each)
uniform float paletteIndex; // Index that tells the shader which palette to use (passed here from Java)

void main()
{
    vec4 color = texture2D(u_texture, v_texCoords);
    vec2 index = vec2(color.r, paletteIndex);
    vec4 indexedColor = texture2D(colorTable, index);
    gl_FragColor = vec4(indexedColor.rgb, color.a); // This way we'll preserve alpha
}