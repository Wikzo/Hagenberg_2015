#version 330 core

// Input vertex data, different for all executions of this shader.
layout(location = 0) in vec3 vertexPosition_modelspace;
layout(location = 1) in vec2 vertexUV;
layout(location = 2) in vec3 vertexNormal_modelspace;

// Output data ; will be interpolated for each fragment.

out vec3 EyeDirection_modelspace;
out vec3 Position_modelspace;

// Values that stay constant for the whole mesh.
uniform mat4 MVP;
uniform mat4 V;
uniform mat4 M;
uniform vec3 LightPosition_worldspace;

void main(){

	// TODO  calculate Modelview Matrix by multiplying model with view Matrix
	mat4 modelViewMatrix = V * M; // <-----
	// TODO  transposing modelview matrix
	modelViewMatrix = transpose(modelViewMatrix); // <-----
	
	// TODO set Position_modelspace:  get the position in model space
	//Position_modelspace = vec3(modelViewMatrix) * Position_modelspace;
	Position_modelspace = (modelViewMatrix * vec4(vertexPosition_modelspace, 1.0)).xyz; // <----- 

	// Output position of the vertex, in clip space : MVP * position
	gl_Position =  MVP * vec4(vertexPosition_modelspace,1);
	
	
	vec3 vertexPosition_cameraspace = ( M*V * vec4(vertexPosition_modelspace,1)).xyz;
	// TODO Vector that goes from the vertex to the camera, in camera space.
	// In camera space, the camera is at the origin (0,0,0).
	
	vec3 vertexToCamera = vec3(0.0,0.0,0.0) - vertexPosition_cameraspace; // <-- vertexToCamera = eye direction in camera space

	// TODO calculate EyeDirection_modelspace: eyedirection in modelspace is the eyedirection in cameraspace multiplied with the inverse inverse transposed 
	// modelview matrix (= transposed modelview matrix because inverted twice)
	EyeDirection_modelspace = (vec4(vertexPosition_cameraspace, 0.0) * transpose(modelViewMatrix)).xyz; // <-- 4th element of vec4 is zero, because we don't want the translation

	// position = 1.0 (we want the translation)
	// vector = 0.0 (we don't want the translation)
}

