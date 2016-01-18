// original

#version 330 core

// Interpolated values from the vertex shaders
in vec2 UV;
in vec4 ShadowCoord;

// Ouput data
layout(location = 0) out vec3 color;

// Values that stay constant for the whole mesh.
uniform sampler2D myTextureSampler;
uniform sampler2DShadow shadowMap;
uniform float time;


void main()
{

	// Light emission properties
	vec3 LightColor = vec3(1, 1, 1);

	// Material properties
	vec3 MaterialDiffuseColor = texture2D(myTextureSampler, UV).rgb;
	vec3 MaterialAmbientColor = vec3(0.1, 0.1, 0.1) * MaterialDiffuseColor;


	float visibility = 1.0;
	float bias = 0.005;

	// old way, does not work with bias
	/*
	// if ( texture( shadowMap, ShadowCoord.xy ).z  <  ShadowCoord.z)
	if (texture(shadowMap, ShadowCoord.xyz) < ShadowCoord.z - bias)
	{
	visibility = 0;
	}
	*/

	visibility -= 0.8 *  (1.0 - texture(shadowMap, vec3(ShadowCoord.xy, (ShadowCoord.z - bias) / ShadowCoord.w)));

	color = MaterialAmbientColor + visibility * MaterialDiffuseColor * LightColor;
	//color = vec3(visibility);
}

