#version 330 core

// Input vertex data, different for all executions of this shader.
layout(location = 0) in vec3 vertexPosition_modelspace;
layout(location = 1) in vec2 vertexUV;
layout(location = 2) in vec3 vertexNormal_modelspace;

// Output data to geometry shader, same as input
out vec3 position_modelspace;
out vec2 uvs;
out vec3 normal_modelspace;

// Values that stay constant for the whole mesh.
uniform mat4 MVP;
uniform mat4 V;
uniform mat4 M;
uniform vec3 LightPosition_worldspace;

void main(){
	position_modelspace = vertexPosition_modelspace;
	uvs = vertexUV;
	normal_modelspace = vertexNormal_modelspace;
}

