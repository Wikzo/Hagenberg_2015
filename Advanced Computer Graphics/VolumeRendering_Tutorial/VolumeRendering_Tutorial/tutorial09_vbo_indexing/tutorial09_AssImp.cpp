// Include standard headers
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <string>

// Include GLEW
#include <GL/glew.h>

//#define MINGW_COMPILER
#define SCREENWIDTH 1280
#define SCREENHEIGHT 1024

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
unsigned char textureToShow = 2;
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

  //      if (effect.lightMatrixId != 0xffffffff)
  //      glUniformMatrix4fv(effect.lightMatrixId, 1, GL_FALSE, &lightMatrix[0][0]);
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

float calculateDist(glm::vec3 center)
{
	glm::vec3 temp = center*center;
	float distance = sqrt(temp.x + temp.y + temp.z);
	return distance;
}

// NOTE: the actual color textures will be on textureIds[1] and up
unsigned int create3DFBO(int width, int height, int nrColorBuffers, std::vector<unsigned int>& textureIds)
{
    // create Framebuffer object
	GLuint framebufferName = 0;
	glGenFramebuffers(1, &framebufferName);
	// bind the newly created framebuffer object
	glBindFramebuffer(GL_FRAMEBUFFER, framebufferName);
	check_gl_error();
    GLenum* drawBuffers = new GLenum[nrColorBuffers];
    
	//this texture won't be used for the 3d texture rendering, we will have to create it anyway
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
	// set the newly created texture as depth attachment for the framebuffer
	glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, renderedDepthTexture, 0);
	// push back the depthtextuer
    textureIds.push_back(renderedDepthTexture);

	// texture to render to
	// number color buffers will be one for now
    for (int i = 0; i < nrColorBuffers; i++)
    {
		// generate a new texture and bind it as 3d-Texture
        GLuint renderedTexture;
        glGenTextures(1, &renderedTexture);
        glBindTexture(GL_TEXTURE_3D, renderedTexture);

		// set and fill the 3d texture with width and height, depth 64
        glTexImage3D(GL_TEXTURE_3D, 0, GL_RGBA, width, height, 64, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_S, GL_REPEAT);//GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_T, GL_REPEAT);//GL_CLAMP_TO_EDGE);
        
		// push this texture back to textureIds too
        textureIds.push_back(renderedTexture);
		// configure the framebuffer. Set the newly created texture as color attachment 0
		glFramebufferTexture3D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0+i, GL_TEXTURE_3D, renderedTexture, 0, i);
        drawBuffers[i] = GL_COLOR_ATTACHMENT0+i;
    }

	// set the drawbuffers for the currently bound framebuffer
	glDrawBuffers(nrColorBuffers, drawBuffers);
	if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
		printf("damn, framebuffer not complete in create3DFBO\n");
	// bind back to screen
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
	float step = 0.9/64.0; // 64 layers of size 0.9
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
		glm::mat4 ViewMatrix = glm::translate(glm::mat4(1.0), glm::vec3(-1.0, -1.5, 0)); // "selfie perspective"
		ViewMatrix = glm::rotate<float>(ViewMatrix, 45.0, glm::vec3(1.0, 1.0, 1.0));
		check_gl_error();

		// check framebuffer status
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			printf("damnit, framebuffer is not complete\n");
		
		// render all the objects - scenes[1] only contains spongy 
		renderObjects(scenes[1], ViewMatrix, ProjectionMatrix, glm::vec3(1.0, 0.0, 0.0), ViewMatrix);

		// set back to main frame buffer
		// draw quads with mesh (create new quad)

		// EXTRA TO SEE SPONGE BOB -------------
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(i % 8 * 64, i / 8 * 64, 64, 64);
		renderObjects(scenes[1], ViewMatrix, ProjectionMatrix, glm::vec3(1.0, 0.0, 0.0), ViewMatrix);
		glBindFramebuffer(GL_FRAMEBUFFER, volumeFramebufferId);
		// EXTRA END -----------------
	}
	// set all changes back:

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
	
	// Accept fragment if it closer to the camera than the former one
	glDepthFunc(GL_LESS);

	glEnable(GL_CULL_FACE);

	glEnable(GL_SAMPLE_ALPHA_TO_COVERAGE);

	GLuint VertexArrayID;
	glGenVertexArrays(1, &VertexArrayID);
	glBindVertexArray(VertexArrayID);

	std::vector<ShaderEffect*> shaderSets;
	std::vector<RenderState*> objects;
	std::vector<RenderState*> bobObjects;

	// Create and compile our GLSL program from the shaders
	std::string contentPath = PATHTOCONTENT;

	// create Framebuffer object for shadow map
    std::vector<unsigned int> renderTextureIds;
    GLuint framebufferName = createFBO(1024, 1024, 3, renderTextureIds);
    unsigned int renderedTexture = renderTextureIds[1];
    unsigned int renderedDepthTexture = renderTextureIds[0];

	std::vector<unsigned int> volumeTexIds;
	GLuint volumeFramebufferName = create3DFBO(64, 64, 1, volumeTexIds);

	check_gl_error();

// ########## load the shader programs ##########
    GLuint bumpProgramID = LoadShaders("shells.vertexshader", "shells.geometryshader", "shells.fragmentshader",  contentPath.c_str());
    VolumeRenderingShaderEffect* bumpProgram = new VolumeRenderingShaderEffect(bumpProgramID);
	shaderSets.push_back(bumpProgram);

	GLuint standardProgramID = LoadShaders("StandardShading2.vertexshader", "StandardShading.fragmentshader",  contentPath.c_str());
    SimpleShaderEffect* standardProgram = new SimpleShaderEffect(standardProgramID);
	shaderSets.push_back(standardProgram);

	// TODO: load the vertex and fragment shader for cube volume rendering (CubeVolumeRendering.vertexshader 
	// and CubeVolumeRendering.fragmentshader)
	GLuint cubeVolumeProgramID = LoadShaders("CubeVolumeRendering.vertexshader.cpp", "CubeVolumeRendering.fragmentshader",  contentPath.c_str());
    SimpleShaderEffect* cubeVolumeProgram = new SimpleShaderEffect(cubeVolumeProgramID);
	shaderSets.push_back(cubeVolumeProgram);

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

	int i = 0;
	for(; i < meshes.size(); i++){
		SimpleRenderState* s = new SimpleRenderState(); //s.set(i, 1, tex, ndotl_ndotv, ndotl_ndoth, ndotl_vdotl, renderTextureIds[1]);
		s->meshId = i;
		s->shaderEffectId = 1;
		s->texId = Texture2;
		objects.push_back(s);
	}
	
	// TODO: create a new mesh and fill it with the createCube Function
	// NOTE: createcube returns a cube where only the insides are visible!
	Mesh* m = new Mesh();
	m->createCube(glm::vec3(1,1,1), false);

	// TODO: push the new mesh back to the meshes
	meshes.push_back(m);
	// TODO: create a new glm::mat4 for the cube and add a small translation (0.0, 3.0, 1.0)
	// so that the cube does not stick in the wall
	glm::mat4 cubeMatrix;
	cubeMatrix = glm::translate(glm::mat4(1.0), glm::vec3(0.0, 3.0, 1.0));
	// TODO: set the cube matrix as modelmatrix of the cube model
	m->modelMatrix = cubeMatrix;

	// TODO: create Renderstate for cube
	for(; i < meshes.size(); i++){
		// TODO: create new VolumeRenderingRenderState
		VolumeRenderingRenderState* obj = new VolumeRenderingRenderState();
		// TODO: assign the shadereffect with the cubevolumerendering shaders to the state
		obj->shaderEffectId = 2;
		// TODO assign the volume texture to the volumeTexId of the state
		obj->volumeTexId = volumeTexIds[1];
		// TODO assign the mesh id of the cube to the meshId
		obj->meshId = i;
		// TODO push back the state to objects
		objects.push_back(obj);
	}

	// TODO: load spongebob
	std::string spongeBobPath = contentPath;
	spongeBobPath += std::string("Spongebob/spongebob_bind.obj");
	Mesh::loadAssImp(spongeBobPath.c_str(), meshes, true);

	// move him out of the walls
	glm::mat4 spongeBobMatrix;
	spongeBobMatrix = glm::translate(glm::mat4(1.0), glm::vec3(1.0, 1.0, 1.0));

	// the meshes of spongebob are appended at the end of the meshes vector
	// TODO: create Renderstate for Bob
	for(; i < meshes.size(); i++){
		// create new SimpleRenderState (no lighting etc)
		SimpleRenderState* s = new SimpleRenderState(); 
		// meshid i
		s->meshId = i;
		// shadereffect is the simple shader
		s->shaderEffectId = 1;
		// set Texture1 (which is the spongebob texture
		s->texId = Texture1;
		// push the Renderstate to the extra object array bobObjects
		bobObjects.push_back(s);
		// set the model matrix for the mesh
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

	std::vector<Scene> scenes;
	// one scene for the landscape and the cube
	scenes.push_back(Scene(&objects, &meshes, &shaderSets));
	// one scene for the spongebob
	scenes.push_back(Scene(&bobObjects, &meshes, &shaderSets));

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

	// render spongebob to texture volumeTexIds[1], using the framebuffer object volumFramebufferName
	// we pass both scenes, although spongebob is in scene [1]
	renderSpongeBobToTexture(scenes, volumeFramebufferName, volumeTexIds[1]);
	
	// render to the framebuffer again, should be redundant, but
	// just to make sure
	glBindFramebuffer(GL_FRAMEBUFFER, 0);


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

		// Compute the MVP matrix from keyboard and mouse input
		computeMatricesFromInputs();
		glm::mat4 ProjectionMatrix = getProjectionMatrix();
		glm::mat4 ViewMatrix = getViewMatrix();
		glm::mat4 ModelMatrix = glm::mat4(1.0);
		glm::mat4 MVP = ProjectionMatrix * ViewMatrix * ModelMatrix;
		check_gl_error();

		// render to the shadow map (nothing at the moment)
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferName);
		//glViewport(0,0,1024,1024);
		glClearColor(0.0, 0.0, 0.0, 0.0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// MINE:
		//renderSpongeBobToTexture(scenes, volumeFramebufferName, volumeTexIds[1]);
		renderObjects(scenes[0], ViewMatrix, ProjectionMatrix, lightPos, lightMVPMatrix);

		
	glViewport(0,0,SCREENWIDTH,SCREENHEIGHT);
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

