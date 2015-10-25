#version 330 core
// OpenGL 3 syntax

layout(location = 0) in vec3 vertexPosition_modelspace;

void main()
{
	// gl_position is a built-in variable that always has to be assigned
	gl_Position.xyz = vertexPosition_modelspace;
	gl_Position.w = 1.0;
}