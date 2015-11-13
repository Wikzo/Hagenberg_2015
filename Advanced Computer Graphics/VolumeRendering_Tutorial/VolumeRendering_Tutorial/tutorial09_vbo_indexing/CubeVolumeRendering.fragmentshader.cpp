#version 330 core

// Interpolated values from the vertex shaders
in vec3 Position_modelspace;
in vec3 EyeDirection_modelspace;

// Ouput data
out vec4 color;

// Values that stay constant for the whole mesh.
uniform sampler3D volumeSampler;

highp float rand(vec2 co)
{
    highp float a = 12.9898;
    highp float b = 78.233;
    highp float c = 43758.5453;
    highp float dt= dot(co.xy ,vec2(a,b));
    highp float sn= mod(dt,3.14);
    return fract(sin(sn) * c);
}

void main(){
	
	// TODO normalize eyedir
	vec3 normalized_eyeDirection = normalize(EyeDirection_modelspace); // <---
	
	// TODO set the start uvs to the position in modelspace of the cube
	vec3 startPoint = Position_modelspace;

	// TODO set the uv step size. A good size is a little bit smaller than one texel to prevent undersampling
	// 64*64 cubes; cube has size of 1
	float stepsize = 1.0 / 64.0;
	vec3 stepVector = vec3(stepsize) * normalized_eyeDirection;

	// TODO calculate the number of steps until we left the cube
	float stepNumber = 128.0; // just to be safe we get all


	

	// calculating the smallest step (in 3D)
	for (int i = 0; i < 3; i++)
	{
		float temp = 0.0;

		if (normalized_eyeDirection[i] > 0)
			temp = (1.0 - startPoint[i]) / stepVector[i];
		else
			temp = -(startPoint[i] / stepVector[i]);

		stepNumber = min(stepNumber, temp);
	}
	
	// TODO  BONUS: calculate a random number between -0.5 and 0.5
	// TODO  BONUS: add this random number to the start uv coordinates to have a slightly different starting
	// point for every ray to trade aliasing for noise

	float random = rand(startPoint.xy);
	random -= 0.5;
	random /= 128.0;
	
	//  a vec4 to store the running color in
	vec4 runningCol = vec4(0.0); // destination
	// TODO  for loop over a fixed amount of steps (at least as much as uvstep fits into the
	// diagonal of the volume texture)
	for (float i = 0; i < stepNumber; i+=1.0)
	{	
		// TODO  sample the volume texture at the texture coordinates
		vec4 sample = texture(volumeSampler, startPoint + random + vec3(i)*stepVector);

		// TODO  mix the color of the new sample with the running color, depending on alpha
		// dst.rgb = dst.rgb * (1.0-sample.a) + sample.rgb * sample.a
		runningCol.rgb = runningCol.rgb * (1.0 - sample.a) + sample.rgb * sample.a;
		
		// TODO  set alpha
		runningCol.a = max(sample.a, runningCol.a);
		//runningCol.a = 1.0;
		
		// TODO  advance the uvs by uvStep
		// TODO  if we are out of the cube, break
		if (i > stepNumber)
			break;
	}
	
	// TODO  set the output color to the running color
	//color.rgba = vec4(1.0, 0.0, 0.0, 1.0);
	color.rgba = runningCol.rgba;
	//color.rgba = vec4(vec3(stepNumber),1.0);

}
