// Include standard headers
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <string>

// Include GLEW - window handling
#include <GL/glew.h>

//#define MINGW_COMPILER
#define SCREENWIDTH 1280
#define SCREENHEIGHT 720

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
unsigned char textureToShow = 2;


struct RenderState
{
	unsigned int meshId;
	unsigned int shaderEffectId;
	unsigned int textureId;
	unsigned int depthMapId;
	unsigned int worldspacePosTexId;
	unsigned int worldspaceNormalTexId;
	unsigned int fluxTexId;
	unsigned int myNormalMapTexId;
	unsigned int cubeTexId;

	// LOAD TEXTURE (4)
	unsigned int testTexId;


	float shininess;
	float metalness;
	float specularity;
	glm::vec3 specularMaterialColor;
    int isShadowCaster;

	RenderState() :
		shininess(8.0f),
			metalness(1.0f),
			specularity(0.3f),
			specularMaterialColor(glm::vec3(0.5, 0.5, 1.0))

	{
	}

	void set(unsigned int mId, unsigned int sId, unsigned int tex1, 
		unsigned int tex2, unsigned int wsTexId, unsigned int wsNormId, unsigned int fluxId, unsigned int testId)
	{
		meshId = mId;
		shaderEffectId = sId;
		textureId = tex1;
		depthMapId = tex2;
		worldspacePosTexId = wsTexId;
		worldspaceNormalTexId = wsNormId;
		fluxTexId = fluxId;
		testTexId = testId;
	}
};

struct Scene
{
	std::vector<RenderState>* objects;
	std::vector<Mesh*>* meshes;
	std::vector<ShaderEffect>* effects;

	Scene(std::vector<RenderState>* _obj,
		std::vector<Mesh*>* _meshes,
		std::vector<ShaderEffect>* _effects) :
			objects(_obj),
			meshes(_meshes),
			effects(_effects)
	{}
};

void renderObjects(Scene& scene, glm::mat4x4& viewMatrix, glm::mat4x4& projectionMatrix, glm::vec3& lightPos, glm::mat4& lightMatrix)
{
	std::vector<RenderState>* objects = scene.objects;
	#ifdef MINGW_COMPILER
	glm::mat4 modelMatrix = glm::rotate(glm::mat4(1.0f), -90.0f, glm::vec3(1.0f, 0.0f, 0.0f));
	#else
	glm::mat4 modelMatrix = glm::mat4(1.0f);
	#endif
	
    check_gl_error();
	for(int i = 0; i < objects->size(); i++)
	{
		RenderState& rs = (*objects)[i];
		unsigned int meshId = (*objects)[i].meshId;
		Mesh* m = (*scene.meshes)[meshId];
		modelMatrix = m->modelMatrix;
		glm::mat4 MVP = projectionMatrix * viewMatrix * modelMatrix;
		// Use our shader
		unsigned int effectId = (*objects)[i].shaderEffectId;
		ShaderEffect& effect = (*scene.effects)[effectId];
		glUseProgram(effect.programId);
        check_gl_error();

		// Send our transformation to the currently bound shader,
		// in the "MVP" uniform
		glUniformMatrix4fv(effect.MVPId, 1, GL_FALSE, &MVP[0][0]);
		glUniformMatrix4fv(effect.MId, 1, GL_FALSE, &modelMatrix[0][0]);
		glUniformMatrix4fv(effect.VId, 1, GL_FALSE, &viewMatrix[0][0]);
        check_gl_error();

		glUniform3f(effect.lightPositionId, lightPos.x, lightPos.y, lightPos.z);
		glUniform1f(effect.shadowMagicNumberId, shadowMagicNumber);
        check_gl_error();

		// Bind our texture in Texture Unit 0
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, rs.textureId);
		// Set our "myTextureSampler" sampler to user Texture Unit 0
		glUniform1i(effect.textureSamplerId, 0);

		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, rs.depthMapId);
		glUniform1i(effect.ndotl_ndotvSamplerId, 1);

		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, rs.worldspacePosTexId);
		glUniform1i(effect.ndotl_ndothSamplerId, 2);

		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, rs.worldspaceNormalTexId);
		glUniform1i(effect.ndotl_vdotlSamplerId, 3);

		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, rs.fluxTexId);
		glUniform1i(effect.fluxId, 4);

		// LOAD TEXTURE (6)
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, rs.testTexId);
		glUniform1i(effect.TestSamplerId, 5);

		check_gl_error();

		// bind the cubemap texture
		//glActiveTexture(GL_TEXTURE2);
		//glBindTexture(GL_TEXTURE_CUBE_MAP, rs.cubeTexId);
		//glUniform1i(effect.cubeSamplerId, 2);
        check_gl_error();

        if (effect.lightMatrixId != 0xffffffff)
        glUniformMatrix4fv(effect.lightMatrixId, 1, GL_FALSE, &lightMatrix[0][0]);
		glUniform1f(effect.shininessId, rs.shininess);
		glUniform1f(effect.metalnessId, rs.metalness);
		glUniform1f(effect.specularityId, rs.specularity);
		glUniform3f(effect.specColorId, rs.specularMaterialColor.r,rs.specularMaterialColor.g, rs.specularMaterialColor.b);
        glUniform1i(effect.isShadowCasterId, rs.isShadowCaster);
        check_gl_error();

		m->bindBuffersAndDraw();


		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		check_gl_error();
	}
}


unsigned int createFBO(int width, int height, int nrColorBuffers, std::vector<unsigned int>& textureIds)
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
    
	// texture to render to
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
        // depth buffer to render to
        check_gl_error();
        textureIds.push_back(renderedTexture);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0+i, renderedTexture,0);
        drawBuffers[i] = GL_COLOR_ATTACHMENT0+i;
    }

    
	
	glDrawBuffers(nrColorBuffers, drawBuffers);
	if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
		printf("fucking shit\n");
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
    delete[] drawBuffers;
    return framebufferName;
}

int main( void )
{
	// Initialise GLFW
	if( !glfwInit() )
	{
		fprintf( stderr, "Failed to initialize GLFW\n" );
		return -1;
	}

	glfwWindowHint(GLFW_SAMPLES, 4);
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

	glEnable(GL_SAMPLE_ALPHA_TO_COVERAGE);

	GLuint VertexArrayID;
	glGenVertexArrays(1, &VertexArrayID);
	glBindVertexArray(VertexArrayID);

	std::vector<ShaderEffect> shaderSets;
	std::vector<RenderState> objects;

	// Create and compile our GLSL program from the shaders
	std::string contentPath = PATHTOCONTENT;

	// create Framebuffer object for shadow map
    std::vector<unsigned int> renderTextureIds;
    GLuint framebufferName = createFBO(1024, 1024, 3, renderTextureIds);
    unsigned int renderedTexture = renderTextureIds[1];
    unsigned int renderedDepthTexture = renderTextureIds[0];

	check_gl_error();

// ########## load the shader programs ##########
    GLuint bumpProgramID = LoadShaders("passThrough.vertexshader.cpp", "tangentsAndStuff.geometryshader.cpp", "TextureBasedShading.fragmentshader.cpp",  contentPath.c_str());
    ShaderEffect bumpProgram = ShaderEffect(bumpProgramID);
	shaderSets.push_back(bumpProgram);

	GLuint location = glGetUniformLocation(bumpProgramID, "NormalSampler");

	GLuint NormalMap1 = loadSoil("smiley.jpg", contentPath.c_str());
	glActiveTexture(GL_TEXTURE5);
	glBindTexture(GL_TEXTURE_2D, NormalMap1);
	glUniform1i(location, 5);


	check_gl_error();

// ########### Load the textures ################ // choose textures HERE

	// LOAD TEXTURE (5)
	GLuint testTexture = loadSoil("stripes_normal.jpg", contentPath.c_str());
	check_gl_error();
	
	GLuint Texture1 = loadSoil("spongebob.DDS", contentPath.c_str());
	check_gl_error();

	GLuint Texture2 = loadSoil("sand_default.png", contentPath.c_str());
	check_gl_error();

	// HERE
	GLuint ndotl_ndotv = loadSoil("ndotl_ndotv.png", contentPath.c_str()); // original: ndotl_ndotv.png ... (also: smiley.jpg)
	check_gl_error();

    GLuint ndotl_ndoth = loadSoil("ndotl_ndoth.png", contentPath.c_str()); // original: ndotl_ndoth.png"
	check_gl_error();

	// this gives blue-black error
	GLuint ndotl_vdotl = loadSoil("ndotl_vdotl.png", contentPath.c_str()); // original: ndotl_vdotl.png
	check_gl_error();

	GLuint cubeMapTex = loadSoilCubeMap(faceFile, contentPath.c_str());
	check_gl_error();
// ############## Load the meshes ###############
	std::vector<Mesh *> meshes;
	std::string modelPath = contentPath;
	#ifdef MINGW_COMPILER
        modelPath += std::string("ACGR_Scene_GI_Unwrap.dae");
	#else
        modelPath += std::string("ACGR_Scene_GI_Unwrap_II.3ds");
	#endif
	Mesh::loadAssImp(modelPath.c_str(), meshes, true);
	
	std::string spongeBobPath = contentPath;
	spongeBobPath += std::string("Spongebob/spongebob_bind.obj");
	Mesh::loadAssImp(spongeBobPath.c_str(), meshes, true);
	glm::mat4 spongeBobMatrix;
	spongeBobMatrix = glm::translate(glm::mat4(1.0), glm::vec3(1.0, 1.0, 1.0));
	
	meshes[meshes.size()-1]->modelMatrix = spongeBobMatrix;
	meshes[meshes.size()-2]->modelMatrix = spongeBobMatrix;
	meshes[meshes.size()-3]->modelMatrix = spongeBobMatrix;
	
	// generate mesh VBOs
	check_gl_error();
	for (int i = 0; i < meshes.size(); i++)
	{
		meshes[i]->generateVBOs();
	}
	check_gl_error();
	// for every mesh set the renderstate
	for(int i = 0; i < meshes.size(); i++)
	{
		uint tex = Texture2;
		// hack! if the mesh belongs to spongebob (last 3 meshes), set the spongebob texture and not the grass
		if (i >= meshes.size()-3) {
			tex = Texture1;
		}

		RenderState obj; obj.set(i, 0, tex, ndotl_ndotv, ndotl_ndoth, ndotl_vdotl, renderTextureIds[1], testTexture);
		
		// LOAD TEXTURE (7)
		//obj.testTexId = testTexture;
		
		objects.push_back(obj);
	}
    
	// for every object make a gui for the texture
    for (int i = 0; i < objects.size(); i++)
    {
        char temp[64];
        sprintf(temp, "Tex Mesh Nr: %i", i);
        //TwAddVarRW(LightGUI, temp  , TW_TYPE_UINT32, &objects[i].textureId, "");
        //TwAddVarRW(LightGUI, temp, TW_TYPE_FLOAT, &objects[i].metalness, "step=0.01");
    }
	check_gl_error();

	std::vector<Scene> scenes;
	scenes.push_back(Scene(&objects, &meshes, &shaderSets));

	// for every object make the materialgui
//    std::vector<TwBar*> materialGuis;
//	for (int i = 0; i < objects.size(); i++)
//	{
//		char temp[64];
//		sprintf(temp, "Mesh Nr: %i", i);
//		TwBar * materialGui = TwNewBar(temp);
//		TwAddVarRW(materialGui, "Shininess", TW_TYPE_FLOAT, &objects[i].shininess, "step=0.05");
//		TwAddVarRW(materialGui, "Metalness", TW_TYPE_FLOAT, &objects[i].metalness, "step=0.01");
//		TwAddVarRW(materialGui, "Specularity", TW_TYPE_FLOAT, &objects[i].specularity, "step=0.01");
//		TwAddVarRW(materialGui, "Specular Material Color", TW_TYPE_COLOR3F, &objects[i].specularMaterialColor, "");
//	}
	check_gl_error();

	// For speed computation
	double lastTime = glfwGetTime();
	int nbFrames = 0;

	do{ 
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

		glClearColor(0.0, 0.0, 1.0, 1.0);
		// Clear the screen
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // compute the MVP matrix for the light
        // worldToView first
        glm::mat4 lightViewMatrix = glm::lookAt(lightPos, lightPos + glm::vec3(1.0, -1.0, 1.0), glm::vec3(0.0, 0.0, 1.0));
        glm::mat4 lightProjMatrix = glm::perspective(90.0f, 1.0f, 2.5f, 100.0f);
        glm::mat4 lightMVPMatrix = lightProjMatrix * lightViewMatrix;

		// move mr sponge bob to the light position
		spongeBobMatrix = glm::translate(glm::mat4(1.0), glm::vec3(lightPos));
		meshes[meshes.size() - 1]->modelMatrix = spongeBobMatrix;
		meshes[meshes.size() - 2]->modelMatrix = spongeBobMatrix;
		meshes[meshes.size() - 3]->modelMatrix = spongeBobMatrix;

		// Compute the MVP matrix from keyboard and mouse input
		computeMatricesFromInputs();
		glm::mat4 ProjectionMatrix = getProjectionMatrix();
		glm::mat4 ViewMatrix = getViewMatrix();
		glm::mat4 ModelMatrix = glm::mat4(1.0);
		glm::mat4 MVP = ProjectionMatrix * ViewMatrix * ModelMatrix;
		check_gl_error();

		// render to the shadow map (nothing at the moment)
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferName);
		glViewport(0,0,1024,1024);
		glClearColor(0.0, 0.0, 0.0, 0.0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// render to the framebuffer again
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0,0,SCREENWIDTH,SCREENHEIGHT);
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

