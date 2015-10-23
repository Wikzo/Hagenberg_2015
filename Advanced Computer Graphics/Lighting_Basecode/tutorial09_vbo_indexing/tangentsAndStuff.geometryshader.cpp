#version 330 core

layout(triangles) in; 
layout(triangle_strip, max_vertices=4) out;

in vec2 uvs[];
in vec3 position_modelspace[];
in vec3 normal_modelspace[];
in vec3 tangent_modelspace[];
in vec3 biTangent_modelspace[];

// Output data ; will be interpolated for each fragment.
out vec2 UV;

out vec3 Position_worldspace;
out vec3 Normal_worldspace;
out vec3 Normal_cameraspace;
out vec3 EyeDirection_cameraspace;
out vec3 LightDirection_cameraspace;
out vec4 myLightUV;

// Values that stay constant for the whole mesh.
uniform mat4 MVP;
uniform mat4 V;
uniform mat4 M;
uniform mat4 worldToLightProjectionMatrix;
uniform vec3 LightPosition_worldspace;

void main(void)
{
	vec3 v0 = position_modelspace[1] - position_modelspace[0];
	vec3 v1 = position_modelspace[2] - position_modelspace[0];
	vec3 faceNorm = cross(v0, v1);
	vec3 absFaceNorm = abs(faceNorm);

	// for every input vertex
	for (int i = 0; i < 3; i++)
	{
		// Output position of the vertex, in clip space : MVP * position
		gl_Position =  MVP * vec4(position_modelspace[i],1);
		
		// Position of the vertex, in worldspace : M * position
		Position_worldspace = (M * vec4(position_modelspace[i],1)).xyz;

		// HERE
		myLightUV = worldToLightProjectionMatrix * vec4(Position_worldspace, 1.0);
		//myLightUV = worldToLightProjectionMatrix*vec4(position_modelspace[i], 1);
		
		// Vector that goes from the vertex to the camera, in camera space.
		// In camera space, the camera is at the origin (0,0,0).
		vec3 vertexPosition_cameraspace = ( V * M * vec4(position_modelspace[i],1)).xyz;
		EyeDirection_cameraspace = vec3(0,0,0) - vertexPosition_cameraspace;

		// Vector that goes from the vertex to the light, in camera space. M is ommited because it's identity.
		vec3 LightPosition_cameraspace = ( V * vec4(LightPosition_worldspace,1)).xyz;
		LightDirection_cameraspace = LightPosition_cameraspace - vertexPosition_cameraspace;
		
		// Normal of the the vertex, in camera space
		Normal_cameraspace = ( V * M * vec4(normal_modelspace[i],0)).xyz; // Only correct if ModelMatrix does not scale the model ! Use its inverse transpose if not.
		Normal_worldspace = ( M * vec4(normal_modelspace[i],0)).xyz;
		
		// UV of the vertex. No special space for this one.
		//UV = position_modelspace[i].xy;
		UV = uvs[i];//position_modelspace[i].yz;

		EmitVertex();
	}
	EndPrimitive();
}