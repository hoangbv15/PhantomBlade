// (Read Only and specific to this vertex|pixel)
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

// Data sent from libgdx SpriteBatch
//(Read Only and the same for all vertex|pixel)
uniform mat4 u_projTrans;

// Variable Data for storing data to pass to fragment Shader
varying vec4 v_color;
varying vec2 v_texCoords;

void main()
{
	// set our varying variables for use in frag shader
	v_color = a_color;
	v_texCoords = a_texCoord0;

	// sgl_Position is a special output variable from
	// openGL that must be set in the vertex shader
	gl_Position =  u_projTrans * a_position;
}