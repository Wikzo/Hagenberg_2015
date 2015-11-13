#version 330 core

layout(triangles) in; 
layout(line_strip, max_vertices=18) out;

in vec2 uvs[];
in vec3 position_modelspace[];
in vec3 normal_modelspace[];
in vec3 tangent_modelspace[];
in vec3 biTangent_modelspace[];

// Output data ; will be interpolated for each fragment.
out vec3 vertexCol;
//out vec2 UV;
//out vec3 Position_worldspace;
//out vec3 Normal_cameraspace;
//out vec3 Tangent_cameraspace;
//out vec3 BiTangent_cameraspace;
//out vec3 EyeDirection_cameraspace;
//out vec3 LightDirection_cameraspace;

// Values that stay constant for the whole mesh.
uniform mat4 MVP;
uniform mat4 V;
uniform mat4 M;
uniform vec3 LightPosition_worldspace;

void main(void)
{
	// for every input vertex
	for (int i = 0; i < 3; i++)
	{
		vec3 tangentHead = position_modelspace[i] + tangent_modelspace[i]*0.04;
		vec3 normalHead = position_modelspace[i] + normal_modelspace[i]*0.04;
		vec3 biTangentHead = position_modelspace[i] + biTangent_modelspace[i]*0.04;
		
		vec4 position_clipspace = MVP * vec4(position_modelspace[i],1);
		
		// normal: blue
		vertexCol = vec3(0.0, 0.0, 1.0);
		// Output position of the vertex, in clip space : MVP * position
		gl_Position =  position_clipspace;
		EmitVertex();
		gl_Position = MVP * vec4(normalHead ,1);
		EmitVertex();
		EndPrimitive();
		
		// tangent: red
		vertexCol = vec3(1.0, 0.0, 0.0);
		// Output position of the vertex, in clip space : MVP * position
		gl_Position =  position_clipspace;
		EmitVertex();
		gl_Position = MVP * vec4(tangentHead ,1);
		EmitVertex();
		EndPrimitive();
		
		// biTangent: green
		vertexCol = vec3(0.0, 1.0, 0.0);
		// Output position of the vertex, in clip space : MVP * position
		gl_Position =  position_clipspace;
		EmitVertex();
		gl_Position = MVP * vec4(biTangentHead ,1);
		EmitVertex();
		EndPrimitive();
	}
}