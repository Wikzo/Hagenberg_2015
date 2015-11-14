// Include standard headers
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <string>

// Include GLEW
#include <GL/glew.h>

//#define MINGW_COMPILER
#define SCREENWIDTH 800
#define SCREENHEIGHT 600

// Include GLFW
#include <glfw3.h>
GLFWwindow* window;

// Include GLM
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
using namespace glm;

#include <common/shader.hpp>
#include <common/texture.hpp>
#include <common/controls.hpp>
#include <common/objloader.hpp>
#include <common/vboindexer.hpp>
#include <common/Mesh.hpp>
#include <common/GLError.h>
#include <common/RenderState.hpp>

// Include AntTweakBar
#include <AntTweakBar.h>

#define PATHTOCONTENT "../tutorial09_vbo_indexing/"

const char *faceFile[6] = {
	"cm_left.bmp",
	"cm_right.bmp",
	"cm_top.bmp",
	"cm_bottom.bmp",
	"cm_back.bmp",
	"cm_front.bmp"
};

float shadowMagicNumber = 0.003;
unsigned char textureToShow = 0;
unsigned char layerToShow = 0;


struct Scene
{
	std::vector<RenderState*>* objects;
	std::vector<Mesh*>* meshes;
	std::vector<ShaderEffect*>* effects;

	Scene(std::vector<RenderState*>* _obj,
		std::vector<Mesh*>* _meshes,
		std::vector<ShaderEffect*>* _effects) :
			objects(_obj),
			meshes(_meshes),
			effects(_effects)
	{}
};

void renderObjects(Scene& scene, glm::mat4x4& viewMatrix, glm::mat4x4& projectionMatrix, glm::vec3& lightPos, glm::mat4& lightMatrix)
{
	std::vector<RenderState*>* objects = scene.objects;
	#ifdef MINGW_COMPILER
	glm::mat4 modelMatrix = glm::rotate(glm::mat4(1.0f), -90.0f, glm::vec3(1.0f, 0.0f, 0.0f));
	#else
	glm::mat4 modelMatrix = glm::mat4(1.0f);
	#endif
	
    check_gl_error();
	for(int i = 0; i < objects->size(); i++)
	{
		RenderState* rs = (*objects)[i];
		
		unsigned int meshId = (*objects)[i]->meshId;
		Mesh* m = (*scene.meshes)[meshId];
		modelMatrix = m->modelMatrix;
		glm::mat4 MVP = projectionMatrix * viewMatrix * modelMatrix;
		// Use our shader
		unsigned int effectId = (*objects)[i]->shaderEffectId;
		ShaderEffect* effect = (*scene.effects)[effectId];
		glUseProgram(effect->programId);
        check_gl_error();
		rs->setParameters(effect);
		// Send our transformation to the currently bound shader,
		// in the "MVP" uniform
		glUniformMatrix4fv(effect->MVPId, 1, GL_FALSE, &MVP[0][0]);
		glUniformMatrix4fv(effect->MId, 1, GL_FALSE, &modelMatrix[0][0]);
		glUniformMatrix4fv(effect->VId, 1, GL_FALSE, &viewMatrix[0][0]);
        check_gl_error();

		//glUniform3f(effect.lightPositionId, lightPos.x, lightPos.y, lightPos.z);
		//glUniform1f(effect.shadowMagicNumberId, shadowMagicNumber);
        check_gl_error();



		

		//glActiveTexture(GL_TEXTURE2);
		//glBindTexture(GL_TEXTURE_2D, rs.worldspacePosTexId);
		//glUniform1i(effect.ndotl_ndothSamplerId, 2);

		//glActiveTexture(GL_TEXTURE3);
		//glBindTexture(GL_TEXTURE_2D, rs.worldspaceNormalTexId);
		//glUniform1i(effect.ndotl_vdotlSamplerId, 3);

		//glActiveTexture(GL_TEXTURE4);
		//glBindTexture(GL_TEXTURE_2D, rs.fluxTexId);
		//glUniform1i(effect.fluxId, 4);
        //check_gl_error();

		// bind the cubemap texture
		//glActiveTexture(GL_TEXTURE2);
		//glBindTexture(GL_TEXTURE_CUBE_MAP, rs.cubeTexId);
		//glUniform1i(effect.cubeSamplerId, 2);
        //check_gl_error();

        if (effect->lightMatrixId != 0xffffffff){
			glm::mat4 lm = lightMatrix * modelMatrix;
			glUniformMatrix4fv(effect->lightMatrixId, 1, GL_FALSE, &lm[0][0]);
		}
		//glUniform1f(effect.shininessId, rs.shininess);
		//glUniform1f(effect.metalnessId, rs.metalness);
		//glUniform1f(effect.specularityId, rs.specularity);
		//glUniform3f(effect.specColorId, rs.specularMaterialColor.r,rs.specularMaterialColor.g, rs.specularMaterialColor.b);
  //      glUniform1i(effect.isShadowCasterId, rs.isShadowCaster);
  //      check_gl_error();

		m->bindBuffersAndDraw();


		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		check_gl_error();
	}
}

// DONE create FBO for Render to texture
unsigned int createFBO(int width, int height, int nrColorBuffers, std::vector<unsigned int>& textureIds)
{
    // create Framebuffer object
	GLuint framebufferName = 0;
	glGenFramebuffers(1, &framebufferName);
	glBindFramebuffer(GL_FRAMEBUFFER, framebufferName);
	check_gl_error();
    GLenum* drawBuffers = new GLenum[nrColorBuffers];
    
	// DONE create a texture to use as the depth buffer of the Framebuffer object
    GLuint renderedDepthTexture;
	glGenTextures(1, &renderedDepthTexture);
	glBindTexture(GL_TEXTURE_2D, renderedDepthTexture);
	// GL_DEPTH_COMPONENT is important to use the texture as depth buffer and as shadow map later
	glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
	check_gl_error();
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	// compare mode and compare func enable hardware shadow mapping. Otherwise the texture lookup would just
	// return the depth value and we would have to do the shadow comparison by ourselves
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
	// configure framebuffer
	glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, renderedDepthTexture, 0);
	// push back the depth texture to the textureids
	// this means the depth texture is id 0!
    textureIds.push_back(renderedDepthTexture);
    
	// texture to render to - the color buffers
    for (int i = 0; i < nrColorBuffers; i++)
    {
        GLuint renderedTexture;
        glGenTextures(1, &renderedTexture);
        glBindTexture(GL_TEXTURE_2D, renderedTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_HALF_FLOAT, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        check_gl_error();
        textureIds.push_back(renderedTexture);
		// configure the Framebuffer to use the texture as color attachment i
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0+i, renderedTexture,0);
		// write into the drawbuffers array that the framebuffer has a color texture at attachment i
        drawBuffers[i] = GL_COLOR_ATTACHMENT0+i;
    }

	// set nrColorBuffers draw buffers for the Framebuffer object
	glDrawBuffers(nrColorBuffers, drawBuffers);
	if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
		printf("damn, end of createFBO\n");
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
    delete[] drawBuffers;
    return framebufferName;
}

float calculateDist(glm::vec3 center)
{
	glm::vec3 temp = center*center;
	float distance = sqrt(temp.x + temp.y + temp.z);
	return distance;
}

unsigned int create3DFBO(int width, int height, int nrColorBuffers, std::vector<unsigned int>& textureIds)
{
    // create Framebuffer object
	GLuint framebufferName = 0;
	glGenFramebuffers(1, &framebufferName);
	glBindFramebuffer(GL_FRAMEBUFFER, framebufferName);
	check_gl_error();
    GLenum* drawBuffers = new GLenum[nrColorBuffers];
    
    GLuint renderedDepthTexture;
	glGenTextures(1, &renderedDepthTexture);
	glBindTexture(GL_TEXTURE_2D, renderedDepthTexture);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
	check_gl_error();
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
	// configure framebuffer
	glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, renderedDepthTexture, 0);
    textureIds.push_back(renderedDepthTexture);

	unsigned char* temp = new unsigned char[128*128*64*4];
    for(int z = 0; z < 64; z++) {
		int idx = z*128*128;
		for(int y = 0; y < 128; y++) {
			int idx2 = idx + 128*y;
			for(int x = 0; x < 128; x++) {
				float distance0 = calculateDist(glm::vec3(abs(x-96.0)/32.0, abs(y-32.0)/32.0, abs(z-16.0)/16.0));
				float distance1 = calculateDist(glm::vec3(abs(x-32.0)/32.0, abs(y-32.0)/32.0, abs(z-16.0)/16.0));
				float distance2 = calculateDist(glm::vec3(abs(x-96.0)/32.0, abs(y-96.0)/32.0, abs(z-16.0)/16.0));
				float distance3 = calculateDist(glm::vec3(abs(x-32.0)/32.0, abs(y-96.0)/32.0, abs(z-16.0)/16.0));
				float distance4 = calculateDist(glm::vec3(abs(x-96.0)/32.0, abs(y-32.0)/32.0, abs(z-48.0)/16.0));
				float distance5 = calculateDist(glm::vec3(abs(x-32.0)/32.0, abs(y-32.0)/32.0, abs(z-48.0)/16.0));
				float distance6 = calculateDist(glm::vec3(abs(x-96.0)/32.0, abs(y-96.0)/32.0, abs(z-48.0)/16.0));
				float distance7 = calculateDist(glm::vec3(abs(x-32.0)/32.0, abs(y-96.0)/32.0, abs(z-48.0)/16.0));
				//float distancex = abs(x-64.0)/128.0;
				//float distancey = abs(y-64.0)/128.0;
				//float distancez = abs(z-32.0)/64.0;

				float distance = min(distance0, distance1);
				distance = min(distance, distance2);
				distance = min(distance, distance3);
				distance = min(distance, distance4);
				distance = min(distance, distance5);
				distance = min(distance, distance6);
				distance = min(distance, distance7);
				//sqrt(distancex*distancex + distancey*distancey + distancez*distancez);
				float alpha = (1.0 - distance) *255;
				alpha = alpha > 128 ? 255:0;


				float red = 255;//sin((float)x/20.0f) * 255;
				float green = 255;//sin((float)y/20.0f) * 255;
				float blue = 255;//sin((float)z/20.0f) * 255;
				//float alpha = ;
				temp[(idx2+x)*4] = (unsigned char)red;
				temp[(idx2+x)*4+1] = (unsigned char)green;
				temp[(idx2+x)*4+2] = (unsigned char)blue;
				temp[(idx2+x)*4+3] = (unsigned char)alpha;
			}
		}
	}

	// texture to render to
    for (int i = 0; i < nrColorBuffers; i++)
    {
        GLuint renderedTexture;
        glGenTextures(1, &renderedTexture);
        glBindTexture(GL_TEXTURE_3D, renderedTexture);


        glTexImage3D(GL_TEXTURE_3D, 0, GL_RGBA, width, height, 64, 0, GL_RGBA, GL_UNSIGNED_BYTE, temp);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_S, GL_REPEAT);//GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_T, GL_REPEAT);//GL_CLAMP_TO_EDGE);
        // depth buffer to render to
        
        textureIds.push_back(renderedTexture);
		glFramebufferTexture3D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0+i, GL_TEXTURE_3D, renderedTexture, 0, i);
        //glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0+i, renderedTexture,0);
        drawBuffers[i] = GL_COLOR_ATTACHMENT0+i;
    }

	delete[] temp;

    
	
	glDrawBuffers(nrColorBuffers, drawBuffers);
	if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
		printf("fucking shit\n");
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
    delete[] drawBuffers;
	check_gl_error();
    return framebufferName;
}


void renderSpongeBobToTexture(std::vector<Scene>& scenes, GLuint volumeFramebufferId, GLuint volumeTextureId)
{
	// TODO disable backface culling
	glDisable(GL_CULL_FACE);
	// TODO bind the volume framebuffer
	glBindFramebuffer(GL_FRAMEBUFFER, volumeFramebufferId);
	// TODO set the viewport to the size of the volume texture (width and height)
	glViewport(0,0,64,64);
	// TODO disable depth test
	glDisable(GL_DEPTH_TEST);
	check_gl_error();

	// size of one layer 
	float step = 0.9/64.0;
	// bake spongebob to texture
	for(int i = 0; i < 64; i++)
	{
		// TODO bind level i of volume texture to the framebuffer object as logical buffer with glFramebufferTexture3D
		glFramebufferTexture3D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_3D, volumeTextureId, 0, i);
		// TODO: set clear color to 0/0/0/0 and clear
		glClearColor(0.0, 0.0, 0.0, 0.0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	check_gl_error();

		// calculate near and far plane 
		float near = -1.8 + step*i;
		float far = near + step*4.0;
		// TODO: ortho matrix to show the whole spongebob (-0.7 to 0.7, with the near and far
		// planes set so that only the current slice is visible
		glm::mat4 ProjectionMatrix = glm::ortho<float>(-0.7, 0.7, -0.7, 0.7, near, far);
		// view matrix so that the spongebob is viewed from a 45degree angle (to have as few
		// triangles perpendicular to the viewing plane as possible
		glm::mat4 ViewMatrix = glm::translate(glm::mat4(1.0), glm::vec3(-1.0, -1.5, 0));
		ViewMatrix = glm::rotate<float>(ViewMatrix, 45.0, glm::vec3(1.0, 1.0, 1.0));
		check_gl_error();

		// check framebuffer status
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			printf("damnit, framebuffer is not complete\n");
		
		// render all the objects - scenes[1] only contains spongy 
		renderObjects(scenes[1], ViewMatrix, ProjectionMatrix, glm::vec3(1.0, 0.0, 0.0), ViewMatrix);
	}
	// TODO: reset clear color
	glClearColor(0.0, 0.0, 1.0, 1.0);
	// TODO: bind screen as framebuffer again (framebufferobject id 0 is screen ;))
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
	// TODO: bind the volume texture
	glBindTexture(GL_TEXTURE_3D, volumeTextureId);
	// TODO: generate Mipmaps with the glGenerateMipmap function 
	glGenerateMipmap(GL_TEXTURE_3D);
	// TODO: reset the viewport
	glViewport(0, 0, SCREENWIDTH, SCREENHEIGHT);
	check_gl_error();
	// TODO reenable culling and depth test
	glEnable(GL_CULL_FACE);
	glEnable(GL_DEPTH_TEST);
}


int main( void )
{
	// Initialise GLFW
	if( !glfwInit() )
	{
		fprintf( stderr, "Failed to initialize GLFW\n" );
		return -1;
	}

	glfwWindowHint(GLFW_SAMPLES, 8);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

	// Open a window and create its OpenGL context
	window = glfwCreateWindow( SCREENWIDTH, SCREENHEIGHT, "Tutorial 09 - VBO Indexing", NULL, NULL);
	if( window == NULL ){
		fprintf( stderr, "Failed to open GLFW window. If you have an Intel GPU, they are not 3.3 compatible. Try the 2.1 version of the tutorials.\n" );
		glfwTerminate();
		return -1;
	}
	glfwMakeContextCurrent(window);

	// Initialize GLEW
	glewExperimental = true; // Needed for core profile
	if (glewInit() != GLEW_OK) {
		fprintf(stderr, "Failed to initialize GLEW\n");
		return -1;
	}

	// Ensure we can capture the escape key being pressed below
	glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);
	glfwSetCursorPos(window, SCREENWIDTH/2, SCREENHEIGHT/2);

	glm::vec3 lightSpecularColor = glm::vec3(0.3, 0.3, 0.3);
	glm::vec3 lightPos = glm::vec3(-4.64,6.7,-5.7);

	// create GUI for light position
	TwInit(TW_OPENGL_CORE, NULL);
	TwWindowSize(SCREENWIDTH, SCREENHEIGHT);
	TwBar * LightGUI = TwNewBar("Light Settings");
	TwAddVarRW(LightGUI, "LightPos X"  , TW_TYPE_FLOAT, &lightPos.x, "step=0.1");
	TwAddVarRW(LightGUI, "LightPos Y"  , TW_TYPE_FLOAT, &lightPos.y, "step=0.1");
	TwAddVarRW(LightGUI, "LightPos Z"  , TW_TYPE_FLOAT, &lightPos.z, "step=0.1");

	TwAddVarRW(LightGUI, "Shadow magic number"  , TW_TYPE_FLOAT, &shadowMagicNumber, "step=0.0001");
    
    TwAddVarRW(LightGUI, "Texture to show"  , TW_TYPE_UINT8, &textureToShow, "");

	TwAddVarRW(LightGUI, "Layer to show"  , TW_TYPE_UINT8, &layerToShow, "");

	// Set GLFW event callbacks. I removed glfwSetWindowSizeCallback for conciseness
	glfwSetMouseButtonCallback(window, (GLFWmousebuttonfun)TwEventMouseButtonGLFW); // - Directly redirect GLFW mouse button events to AntTweakBar
	glfwSetCursorPosCallback(window, (GLFWcursorposfun)TwEventMousePosGLFW);          // - Directly redirect GLFW mouse position events to AntTweakBar
	glfwSetScrollCallback(window, (GLFWscrollfun)TwEventMouseWheelGLFW);    // - Directly redirect GLFW mouse wheel events to AntTweakBar
	glfwSetKeyCallback(window, (GLFWkeyfun)TwEventKeyGLFW);                         // - Directly redirect GLFW key events to AntTweakBar
	glfwSetCharCallback(window, (GLFWcharfun)TwEventCharGLFW);                      // - Directly redirect GLFW char events to AntTweakBar

	// Dark blue background
	glClearColor(0.0f, 0.0f, 0.4f, 0.0f);

	// Enable depth test
	glEnable(GL_DEPTH_TEST);
	glViewport(0, 0, SCREENWIDTH, SCREENHEIGHT);
	//
	// Accept fragment if it closer to the camera than the former one
	glDepthFunc(GL_LESS);

	//glEnable(GL_CULL_FACE);

	glEnable(GL_SAMPLE_ALPHA_TO_COVERAGE);

	GLuint VertexArrayID;
	glGenVertexArrays(1, &VertexArrayID);
	glBindVertexArray(VertexArrayID);

	std::vector<ShaderEffect*> shaderSets;
	std::vector<RenderState*> objects;
	std::vector<RenderState*> rttObjects;
	std::vector<RenderState*> bobObjects;

	// Create and compile our GLSL program from the shaders
	std::string contentPath = PATHTOCONTENT;

	// DONE create Framebuffer object for shadow map
    std::vector<unsigned int> lightRenderTextureIds;
    GLuint lightFramebufferName = createFBO(1024, 1024, 3, lightRenderTextureIds);
    unsigned int renderedTexture = lightRenderTextureIds[1];
    unsigned int renderedDepthTexture = lightRenderTextureIds[0];

	std::vector<unsigned int> volumeTexIds;
	GLuint volumeFramebufferName = create3DFBO(64, 64, 1, volumeTexIds);

	check_gl_error();

// ########## load the shader programs ##########

	GLuint standardProgramID = LoadShaders("StandardShading2.vertexshader", "StandardShading.fragmentshader",  contentPath.c_str());
    SimpleShaderEffect* standardProgram = new SimpleShaderEffect(standardProgramID);
	shaderSets.push_back(standardProgram);

	// DONE: Load the shaders for the light output and create a simple shader effect
	GLuint rttProgramID = LoadShaders("lightOutput.vertexshader", "lightOutput.fragmentshader",  contentPath.c_str());
    SimpleShaderEffect* rttProgram = new SimpleShaderEffect(rttProgramID);
	shaderSets.push_back(rttProgram);

	// DONE Load shaders for just drawing a quad on the screen
	GLuint screenQuadProgramID = LoadShaders("texturedQuad.vertexshader", "texturedQuad.fragmentshader", contentPath.c_str());
	SimpleShaderEffect* screenQuadProgram = new SimpleShaderEffect(screenQuadProgramID);
	shaderSets.push_back(screenQuadProgram);
	
	// TODO: LOAD THE SHADOW MAPPING shaders and create a shadow mapping Shader effect and push this back to shaderSets

	enum ShaderEffects {
		STANDARDSHADING = 0,
		LIGHT_OUTPUT,
		TEXTURED_QUAD,
		SHADOW_MAPPING
	};

	check_gl_error();

// ########### Load the textures ################
	GLuint Texture1 = loadSoil("spongebob.DDS", contentPath.c_str());
	check_gl_error();

	GLuint Texture2 = loadSoil("lichenStone.dds", contentPath.c_str());
	check_gl_error();

	GLuint ndotl_ndotv = loadSoil("ndotl_ndotv.png", contentPath.c_str());
	check_gl_error();

    GLuint ndotl_ndoth = loadSoil("ndotl_ndoth.png", contentPath.c_str());
	check_gl_error();

	GLuint ndotl_vdotl = loadSoil("ndotl_vdotl.png", contentPath.c_str());
	check_gl_error();

	GLuint cubeMapTex = loadSoilCubeMap(faceFile, contentPath.c_str());
	check_gl_error();
// ############## Load the meshes ###############
	std::vector<Mesh *> meshes;
	std::vector<Mesh *> spongeBobMeshes;
	std::string modelPath = contentPath;
	#ifdef MINGW_COMPILER
        modelPath += std::string("ACGR_Scene_GI_Unwrap.dae");
	#else
        modelPath += std::string("ACGR_Scene_GI_Unwrap_II.3ds");
	#endif
	Mesh::loadAssImp(modelPath.c_str(), meshes, true);	

	int spongeMeshStartId = meshes.size();
	std::string spongeBobPath = contentPath;
	spongeBobPath += std::string("Spongebob/spongebob_bind.obj");
	Mesh::loadAssImp(spongeBobPath.c_str(), meshes, true);

	

	glm::mat4 spongeBobMatrix;
	spongeBobMatrix = glm::translate(glm::mat4(1.0), glm::vec3(1.0, 1.0, 1.0));
	
	for(int i = 0; i < meshes.size(); i++){
		// DONE create a SimpleRenderstate for all objects which should cast shadows
		SimpleRenderState* rtts = new SimpleRenderState();
		rtts->meshId = i;
		rtts->shaderEffectId = LIGHT_OUTPUT; // the Render to texture shader effect
		rtts->texId = Texture2;
		rttObjects.push_back(rtts);

		// TODO change to a Shadowmapping renderstate for all objects which should receive shadows
		SimpleRenderState* s = new SimpleRenderState(); 
		s->meshId = i;
		s->shaderEffectId = STANDARDSHADING;
		s->texId = Texture2;
		objects.push_back(s);
	}

	// apply the modelmatrix to the spongebob meshes
	for(int i = spongeMeshStartId; i < meshes.size(); i++){
			meshes[i]->modelMatrix = spongeBobMatrix;
	}
	
	// generate mesh VBOs
	check_gl_error();
	for (int i = 0; i < meshes.size(); i++)
	{
		meshes[i]->generateVBOs();
	}
	check_gl_error();
    
	// for every object make a gui for the texture
    for (int i = 0; i < objects.size(); i++)
    {
        char temp[64];
        sprintf(temp, "Tex Mesh Nr: %i", i);
        //TwAddVarRW(LightGUI, temp  , TW_TYPE_UINT32, &objects[i].textureId, "");
        //TwAddVarRW(LightGUI, temp, TW_TYPE_FLOAT, &objects[i].metalness, "step=0.01");
    }
	check_gl_error();

	// DONE create a quad mesh which is about a quarter of the screen big and in the middle of the right side
	Mesh m; m.createQuad(vec2(0.0, -0.5), vec2(1.0, 0.5));
	m.generateVBOs();
	meshes.push_back(&m);
	// DONE create a SimpleRenderstate for the quad (just has to show a texture)
	SimpleRenderState* screenQuadState = new SimpleRenderState(); 
	screenQuadState->meshId = meshes.size()-1;
	screenQuadState->shaderEffectId = TEXTURED_QUAD;
	screenQuadState->texId = Texture2;
	objects.push_back(screenQuadState);


	std::vector<Scene> scenes;
	// one scene for the landscape and the cube
	scenes.push_back(Scene(&objects, &meshes, &shaderSets));
	// one scene for the render to texture pass
	scenes.push_back(Scene(&rttObjects, &meshes, &shaderSets));

	check_gl_error();


	// For speed computation
	double lastTime = glfwGetTime();
	int nbFrames = 0;

	do{ 
		// apply the shadow depth map to the textured quad object to debug
		// with the gui we can change which texture we see
		SimpleRenderState* quadObj = static_cast<SimpleRenderState*>(objects[objects.size()-1]);
		quadObj->texId = lightRenderTextureIds[textureToShow];

		// Measure speed
		double currentTime = glfwGetTime();
		nbFrames++;
		if ( currentTime - lastTime >= 1.0 ){ // If last prinf() was more than 1sec ago
			// printf and reset
			printf("%f ms/frame\n", 1000.0/double(nbFrames));
			nbFrames = 0;
			lastTime += 1.0;
		}

		check_gl_error();
		// Clear the screen
		glClearColor(0.0, 0.0, 0.5, 1.0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		// Compute the MVP matrix from keyboard and mouse input
		computeMatricesFromInputs();
		glm::mat4 ProjectionMatrix = getProjectionMatrix();
		glm::mat4 ViewMatrix = getViewMatrix();
		check_gl_error();

        // compute the MVP matrix for the light
        // worldToView first
        glm::mat4 lightViewMatrix = glm::lookAt(lightPos, lightPos + glm::vec3(0.5, -1.0, 0.5), glm::vec3(0.0, 0.0, 1.0));
        glm::mat4 lightProjMatrix = glm::perspective(90.0f, 1.0f, 2.5f, 100.0f);
        glm::mat4 lightMVPMatrix = lightProjMatrix * lightViewMatrix;

		
		// WE NEED TO DO THIS --------------- 
		// (see slides "Before actual rendering, render the second scene from the point of view of the light")
		// TODO: render scene[1] to the shadow map 
		// bind framebuffer, bind the viewport, set clearcolor and clear the screen first
		// afterwards, bind back to the screen framebuffer (id 0) and set the screen viewport (SCREENWIDTH, SCREENHEIGHT)
		glBindFramebuffer(GL_FRAMEBUFFER, lightFramebufferName);
		glViewport(0, 0, 1024, 1024);
		glClearColor(0.0, 0.0, 0.0, 0.0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		renderObjects(scenes[1], lightViewMatrix, lightProjMatrix, lightPos, lightMVPMatrix);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, SCREENWIDTH, SCREENHEIGHT);

		// set the scene constant variales (shadow bias and light position)
		ShadowMappingRenderState::shadowMagicNumber = shadowMagicNumber;
		SimpleRenderState::lightPositionWorldSpace = lightPos;
		// render to the screen buffer
		renderObjects(scenes[0], ViewMatrix, ProjectionMatrix, lightPos, lightMVPMatrix);

		
		// draw gui
		TwDraw();
		// Swap buffers
		glfwSwapBuffers(window);
		glfwPollEvents();

	} // Check if the ESC key was pressed or the window was closed
	while( glfwGetKey(window, GLFW_KEY_ESCAPE ) != GLFW_PRESS &&
		   glfwWindowShouldClose(window) == 0 );

	// Cleanup VBO and shader
	//glDeleteProgram(programID);
	glDeleteTextures(1, &Texture1);
	//glDeleteTextures(1, &Texture2);
	//glDeleteTextures(1, &Texture3);
	glDeleteVertexArrays(1, &VertexArrayID);


	TwTerminate();
	// Close OpenGL window and terminate GLFW
	glfwTerminate();

	return 0;
}

