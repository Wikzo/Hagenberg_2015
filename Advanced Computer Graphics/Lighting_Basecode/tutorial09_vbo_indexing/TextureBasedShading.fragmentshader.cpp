// ORIGINAL:
#version 330 core

// Interpolated values from the vertex shaders
in vec2 UV;

in vec3 Position_worldspace;
in vec3 Normal_cameraspace;
in vec3 Normal_worldspace;
in vec3 EyeDirection_cameraspace;
in vec3 LightDirection_cameraspace;

// mine:
in vec3 EyeDirection_tangentSpace;
in vec3 LightDirection_tangentSpace;

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

	/*float Eta = 0.67;          // Ratio of indices of refraction (air -> glass)
	//float FresnelPower = 0.5; // Controls degree of reflectivity at grazing angles
	float F = ((1.0 - Eta) * (1.0 - Eta)) / ((1.0 + Eta) * (1.0 + Eta));
	float base = 1.0 - dot(halfwayDirection, viewDirection);
	float exponential = pow(base, fresnelPower);
	float fresnel = exponential + F * (1.0 - exponential);

	return fresnel;*/

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

	vec2 uvs = UV.xy; // minus or not??
	uvs.y = uvs.y;
	// Material properties
	vec4 col0 = texture(myTextureSampler, uvs.xy);

	vec3 MaterialDiffuseColor = col0.rgb;
	vec3 MaterialAmbientColor = vec3(0.1, 0.1, 0.1) * MaterialDiffuseColor;
	vec3 specColor = mix(MaterialSpecularColor, MaterialDiffuseColor, metalness);
	//specColor = specularity * specColor;
	//vec3 specColor = vec3(0.3, 0.3, 0.3);

	// Distance to the light
	float distance = length(LightPosition_worldspace - Position_worldspace);
	LightPower /= distance*distance;

	// CALCULATING NORMALS VIA NORMAL MAP TEXTURE
	// Need to use tangent space.
	// color goes from 0 to 1
	// normal goes from -1 to +1
	// to convert: Normal = (Color*2) - 1
	vec3 normalMap = normalize(texture2D(MyNormalMapSampler, uvs.xy).rgb*2.0 - 1.0);

	vec3 l = normalize(LightDirection_tangentSpace);

	//vec3 n = normalize(Normal_cameraspace);
	vec3 n = normalMap; // <--- applying the normal map
	//n = vec3(0.0);

	// anisotropic reflective normals - does not work...
	// https://www.udacity.com/course/viewer#!/c-cs291/l-124106597/e-176585825/m-176585827
	/*float groove = 1.0;
	for (int i = 0; i < 2; i++)
	{
		vec3 offset = (i == 0) ? Position_worldspace : -Position_worldspace;
		offset.y = 0;

		vec3 jiggledNormal = normalize(n + groove * normalize(offset));
		n = jiggledNormal;
	}*/

		// Eye vector (towards the camera)
		vec3 E = normalize(EyeDirection_tangentSpace);

		vec3 halfwayDirection = normalize(l + E);

		// lambert
		float ndotL = dot(n, l);


		ndotL = pow(ndotL, 10.0);
		ndotL = clamp(ndotL, 0.0, 1.0);

		float ndotV = dot(n, E);
		ndotV = pow(ndotV, 10.0);
		ndotV = clamp(ndotV, 0.0, 1.0);

		// blin
		float ndotH = dot(n, halfwayDirection);
		ndotH = pow(ndotH, 10.0);
		ndotH = clamp(ndotH, 0.0, 1.0);

		float vdotL = dot(E, l);
		vdotL = pow(vdotL, 10.0);
		vdotL = clamp(vdotL, 0.0, 1.0);

		vec3 diffuseLight = texture(ndotvSampler, vec2(ndotL, ndotV)).rgb;
		vec3 specularLight = texture(ndothSampler, vec2(ndotL, ndotH)).rgb * 4.0;
		vec3 retroreflectiveLight = texture(vdotlSampler, vec2(ndotL, vdotL)).rgb*2.0;

		diffuseLight = LightPower*  MaterialDiffuseColor * diffuseLight;


		diffuseLight = texture2D(myTextureSampler, uvs).rgb * ndotL;


		specularLight = LightPower * vec3(LightColor) * vec3(specColor)
			//* mix(vec3(specColor), vec3(1.0), w)
			* specularLight;
		retroreflectiveLight = vec3(0.0);//;LightPower * retroreflectiveLight;


		// CALCULATING FRESNEL SPECULARITY
		//float fresnel = SchlickFresnelFactor(halfwayDirection, E, 5.0);
		//specularLight *= fresnel; // <-- applying fresnell effect


		float lightingMul = 1.0;//pow(max(ndotL, 0.0), 0.5);

		//color.rgb =
			//(MaterialAmbientColor +	(diffuseLight + specularLight) * lightingMul);

		color.rgb = diffuseLight;

		color.a = 1.0f;
		//color.rgb = vec3(fresnel);

		//color.rgb = texture(MyNormalMapSampler, UV).rgb;

		//color.rgb = vec3(ndotL);
	
}