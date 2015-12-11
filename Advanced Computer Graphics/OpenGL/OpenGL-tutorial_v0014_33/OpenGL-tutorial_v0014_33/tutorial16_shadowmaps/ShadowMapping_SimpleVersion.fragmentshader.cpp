#version 330 core

// Interpolated values from the vertex shaders
in vec2 UV;
in vec4 ShadowCoord;

// Ouput data
layout(location = 0) out vec3 color;

// Values that stay constant for the whole mesh.
uniform sampler2D myTextureSampler;
uniform sampler2DShadow shadowMap;

void main(){

	// Light emission properties
	vec3 LightColor = vec3(1,1,1);
	
	// Material properties
	vec3 MaterialDiffuseColor = texture2D( myTextureSampler, UV ).rgb;

	//float visibility = texture( shadowMap, vec3(ShadowCoord.xy, (ShadowCoord.z)/ShadowCoord.w) );

	// http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-16-shadow-mapping/
	float visibility = 1.0;
	if (texture(shadowMap, ShadowCoord.xyz)  <  ShadowCoord.z)
	{
		visibility = 0.5;
	}
	 

	//color = visibility * MaterialDiffuseColor * LightColor;
	color = vec3(visibility);
}