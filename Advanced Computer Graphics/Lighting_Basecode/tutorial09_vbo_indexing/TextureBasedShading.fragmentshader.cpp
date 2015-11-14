// ORIGINAL:
#version 330 core

// Interpolated values from the vertex shaders
in vec2 UV;

in vec3 Position_worldspace;
in vec3 Normal_cameraspace;
in vec3 Normal_worldspace;
in vec3 EyeDirection_cameraspace;
in vec3 LightDirection_cameraspace;

// Ouput data
layout(location = 0) out vec4 color;

// Values that stay constant for the whole mesh.
uniform sampler2D myTextureSampler;
uniform sampler2D ndotvSampler;

// LOAD TEXTURE (1)
uniform sampler2D MyNormalMapSampler;

uniform sampler2D ndothSampler;
uniform sampler2D vdotlSampler;
uniform sampler2D textureSampler5;
uniform mat4 MV;
uniform vec3 LightPosition_worldspace;
uniform vec3 MaterialSpecularColor;
uniform float metalness;
uniform float shininess;
uniform float specularity;
uniform int isShadowCaster;
uniform float shadowMagicNumber;

float SchlickFresnelFactor(vec3 halfwayDirection, vec3 viewDirection, float fresnelPower)
{
	// http://filmicgames.com/archives/557
	// http://kylehalladay.com/blog/tutorial/2014/02/18/Fresnel-Shaders-From-The-Ground-Up.html
	// https://en.wikibooks.org/wiki/GLSL_Programming/Unity/Specular_Highlights_at_Silhouettes
	// http://www.horde3d.org/wiki/index.php5?title=Shading_Technique_-_Fresnel
	/*
	float base = 1 – dot(V,H);
	float exponential = pow( base, 5.0);
	float fresnel = exponential + F0 * (1.0 – exponential);
	specVal *= fresnel;
	*/

	float Eta = 0.67;          // Ratio of indices of refraction (air -> glass)
	//float FresnelPower = 0.5; // Controls degree of reflectivity at grazing angles
	float F = ((1.0 - Eta) * (1.0 - Eta)) / ((1.0 + Eta) * (1.0 + Eta));
	float base = 1.0 - dot(halfwayDirection, viewDirection);
	float exponential = pow(base, fresnelPower);
	float fresnel = exponential + F * (1.0 - exponential);

	//return fresnel;

	float w = pow(1.0 - max(0.0,
		dot(halfwayDirection, viewDirection)), fresnelPower);
	return w;
}


void main()
{

	// Light emission properties
	// You probably want to put them as uniforms
	vec3 LightColor = vec3(1, 1, 1);
	float LightPower = 50.0f;

	vec2 uvs = UV.xy;
	uvs.y = -uvs.y;
	// Material properties
	vec4 col0 = texture(myTextureSampler, uvs.xy);

	vec3 MaterialDiffuseColor = col0.rgb;
	vec3 MaterialAmbientColor = vec3(0.1, 0.1, 0.1) * MaterialDiffuseColor;
	vec3 specColor = mix(MaterialSpecularColor, MaterialDiffuseColor, metalness);
	//specColor = specularity * specColor;

	// Distance to the light
	float distance = length(LightPosition_worldspace - Position_worldspace);
	LightPower /= distance*distance;

	// calculating the normal map
	// color goes from 0 to 1
	// normal goes from -1 to +1
	// to convert: Normal = (Color*2) - 1
	vec3 normalMap = normalize(texture2D(MyNormalMapSampler, uvs.xy).rgb*2.0 - 1.0);

	vec3 l = normalize(LightDirection_cameraspace);
	//vec3 n = normalize(Normal_cameraspace);
	vec3 n = normalMap; // <--- applying the normal map

	// Eye vector (towards the camera)
	vec3 E = normalize(EyeDirection_cameraspace);

	vec3 halfwayDirection = normalize(l + E);

	float ndotL = dot(n, l);
	ndotL = pow(ndotL, 10.0);
	ndotL = clamp(ndotL, 0.0, 1.0);
	float ndotV = dot(n, E);
	ndotV = pow(ndotV, 10.0);
	ndotV = clamp(ndotV, 0.0, 1.0);
	float ndotH = dot(n, halfwayDirection);
	ndotH = pow(ndotH, 10.0);
	ndotH = clamp(ndotH, 0.0, 1.0);
	float vdotL = dot(E, l);
	vdotL = pow(vdotL, 10.0);
	vdotL = clamp(vdotL, 0.0, 1.0);

	vec3 diffuseLight = texture(ndotvSampler, vec2(ndotL, ndotV)).rgb;
	vec3 specularLight =  texture(ndothSampler, vec2(ndotL, ndotH)).rgb * 4.0;

	

	vec3 retroreflectiveLight = texture(vdotlSampler, vec2(ndotL, vdotL)).rgb*2.0;

	diffuseLight = LightPower*  MaterialDiffuseColor * diffuseLight;
	specularLight = LightPower * vec3(LightColor) * vec3(specColor)
		//* mix(vec3(specColor), vec3(1.0), w)
		* specularLight;
	retroreflectiveLight = LightPower * retroreflectiveLight;

	float fresnel = SchlickFresnelFactor(halfwayDirection, E,1.5);
	specularLight *= fresnel;
	//specularLight= vec3(fresnel,0.0,0.0);


	float lightingMul = 1.0;//pow(max(ndotL, 0.0), 0.5);

	color.xyz =
		MaterialAmbientColor +
		(diffuseLight +
			specularLight + retroreflectiveLight) * lightingMul;
	color.a = 1.0f;//texture( myTextureSampler, UV ).a;

				   //color.xyz = specularLight;//texture(ndothSampler, vec2(ndotL, ndotH)).rgb * 1.0;;//texture(ndothSampler, vec2(-ndotL, -ndotH)).rgb;//pow(ndotH, 6.0);;//

	
	color.rgb = vec3(fresnel);

	//color.rgb = texture(MyNormalMapSampler, UV).rgb;
}