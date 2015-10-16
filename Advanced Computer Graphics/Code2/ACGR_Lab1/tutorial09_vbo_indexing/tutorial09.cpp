// Include standard headers
#include <stdio.h>
#include <stdlib.h>
#include <vector>

// Include GLEW
#include <GL/glew.h>

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

#define PATHTOCONTENT "../tutorial09_vbo_indexing/"

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
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

	// Open a window and create its OpenGL context
	window = glfwCreateWindow( 1024, 768, "Tutorial 09 - VBO Indexing", NULL, NULL);
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
	glfwSetCursorPos(window, 1024/2, 768/2);

	// Dark blue background
	glClearColor(0.0f, 0.0f, 0.4f, 0.0f);

	// Enable depth test
	glEnable(GL_DEPTH_TEST);
	// Accept fragment if it closer to the camera than the former one
	glDepthFunc(GL_LESS); 

	// Cull triangles which normal is not towards the camera
	glEnable(GL_CULL_FACE);

	// TODO: alpha blend mode enable (for shells)
	//glEnable(GL_BLEND);
	//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	GLuint VertexArrayID;
	glGenVertexArrays(1, &VertexArrayID);
	glBindVertexArray(VertexArrayID);

	// Create and compile our GLSL program from the shaders
	std::string contentPath = PATHTOCONTENT;
	std::string vertexShaderPath = contentPath;
	vertexShaderPath += std::string("StandardShading.vertexshader.cpp");//contentPath.append("StandardShading.vertexshader");
	std::string fragmentShaderPath = contentPath;
	fragmentShaderPath += std::string("StandardShading.fragmentshader.cpp");
	std::string geometryShaderPath = contentPath;
	geometryShaderPath += std::string("passThrough.geometryshader.cpp");
	GLuint programID = LoadShaders(vertexShaderPath.c_str(), geometryShaderPath.c_str(), fragmentShaderPath.c_str());

	// Get a handle for our "MVP" uniform
	GLuint MatrixID = glGetUniformLocation(programID, "MVP");
	GLuint ViewMatrixID = glGetUniformLocation(programID, "V");
	GLuint ModelMatrixID = glGetUniformLocation(programID, "M");

	// Load thglEnablee texture
	std::string texturePath = contentPath;
	texturePath += std::string("spongebob.DDS");
	GLuint Texture = loadDDS(texturePath.c_str());
	
	// Get a handle for our "myTextureSampler" uniform
	GLuint TextureID  = glGetUniformLocation(programID, "myTextureSampler");

	// Read our .obj file
	std::vector<glm::vec3> vertices;
	std::vector<glm::vec2> uvs;
	std::vector<glm::vec3> normals;
	std::string modelPath = contentPath;
	modelPath += std::string("Spongebob/spongebob_bind.obj");
	bool res = loadOBJ(modelPath.c_str(), vertices, uvs, normals);

	///TODO: now the object is loaded and all the information available in vertices, uvs and normals
	/// vertices, uvs and normals are vectors of equal size, always three consecutive vertices (and normals and uvs)
	/// form one triangle
	/// in this place you can do whatever you want with them ;)

	/// save all normals for the vertices
	for (int i = 0; i < vertices.size(); i += 3)
	{
		///save normal x3 for the plane
		normals[i] = glm::cross(vertices[i + 1] - vertices[i], vertices[i + 2] - vertices[i + 1]);
		normals[i + 1] = glm::cross(vertices[i + 1] - vertices[i], vertices[i + 2] - vertices[i + 1]);
		normals[i + 2] = glm::cross(vertices[i + 1] - vertices[i], vertices[i + 2] - vertices[i + 1]);
	}

	// using smooth normals
	/*for (int i = 0; i < vertices.size(); i++)
	{
		//create vector for smoothed normals
		glm::vec3 smooth_normals = normals[i];
		//loop thourh all vertices (j)
		for (int j = 0; j < vertices.size(); j++)
		{
			//compare vertices i and j
			if (vertices[i] == vertices[j])
			{
				//set smoothed normals to the value of normals calculated
				smooth_normals += normals[j];
			}
		}
		normals[i] = normalize(smooth_normals);
	}*/


	//printf("loop nr %i\n", i);

	// fins
	/*int vertices_size = vertices.size();
	for (size_t i = 0; i < vertices_size; i += 3)
	{
		// vertices - triangle 1
		glm::vec3 temp1 = vertices[i]; // point 0
		glm::vec3 temp2 = vertices[i] + normals[i] * 0.1f; // point 0 raised
		glm::vec3 temp3 = vertices[i + 1]; // point 1

										   // vertices - triangle 2
		glm::vec3 temp2_1 = temp3; // point 1
		glm::vec3 temp2_2 = temp3 + normals[i] * 0.1f; // point 1 raised
		glm::vec3 temp2_3 = temp2; // point 0 raised

		vertices.push_back(temp1);
		vertices.push_back(temp2);
		vertices.push_back(temp3);

		vertices.push_back(temp2_1);
		vertices.push_back(temp2_2);
		vertices.push_back(temp2_3);

		// UVs
		uvs.push_back(glm::vec2(0, 0));
		uvs.push_back(glm::vec2(0, 1));
		uvs.push_back(glm::vec2(1, 0));

		uvs.push_back(glm::vec2(1, 0));
		uvs.push_back(glm::vec2(1, 1));
		uvs.push_back(glm::vec2(0, 1));

		// normals
		glm::vec3 normal1 = normals[i];

		normals.push_back(normal1);
		normals.push_back(normal1);
		normals.push_back(normal1);

		normals.push_back(normal1);
		normals.push_back(normal1);
		normals.push_back(normal1);
	}*/

	// shells - move outwards and make more transparent
	int vertices_size = vertices.size();
	for (size_t i = 0; i < vertices_size; i += 3)
	{
		vertices[i] = vertices[i] + normals[i] * 0.1f;
		vertices[i + 1] = vertices[i + 1] + normals[i + 1] * 0.1f;
		vertices[i + 2] = vertices[i + 2] + normals[i + 2] * 0.1f;

		//depth.push_back(1.0f);

	}

	std::vector<unsigned short> indices;
	std::vector<glm::vec3> indexed_vertices;
	std::vector<glm::vec2> indexed_uvs;
	std::vector<glm::vec3> indexed_normals;
	indexVBO(vertices, uvs, normals, indices, indexed_vertices, indexed_uvs, indexed_normals);

	// Load it into a VBO

	GLuint vertexbuffer;
	glGenBuffers(1, &vertexbuffer);
	glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
	glBufferData(GL_ARRAY_BUFFER, indexed_vertices.size() * sizeof(glm::vec3), &indexed_vertices[0], GL_STATIC_DRAW);

	GLuint uvbuffer;
	glGenBuffers(1, &uvbuffer);
	glBindBuffer(GL_ARRAY_BUFFER, uvbuffer);
	glBufferData(GL_ARRAY_BUFFER, indexed_uvs.size() * sizeof(glm::vec2), &indexed_uvs[0], GL_STATIC_DRAW);

	GLuint normalbuffer;
	glGenBuffers(1, &normalbuffer);
	glBindBuffer(GL_ARRAY_BUFFER, normalbuffer);
	glBufferData(GL_ARRAY_BUFFER, indexed_normals.size() * sizeof(glm::vec3), &indexed_normals[0], GL_STATIC_DRAW);

	// Generate a buffer for the indices as well
	GLuint elementbuffer;
	glGenBuffers(1, &elementbuffer);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementbuffer);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size() * sizeof(unsigned short), &indices[0] , GL_STATIC_DRAW);

	// Get a handle for our "LightPosition" uniform
	glUseProgram(programID);
	GLuint LightID = glGetUniformLocation(programID, "LightPosition_worldspace");

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

		// Clear the screen
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Use our shader
		glUseProgram(programID);

		// Compute the MVP matrix from keyboard and mouse input
		computeMatricesFromInputs();
		glm::mat4 ProjectionMatrix = getProjectionMatrix();
		glm::mat4 ViewMatrix = getViewMatrix();
		glm::mat4 ModelMatrix = glm::mat4(1.0);
		glm::mat4 MVP = ProjectionMatrix * ViewMatrix * ModelMatrix;

		// Send our transformation to the currently bound shader, 
		// in the "MVP" uniform
		glUniformMatrix4fv(MatrixID, 1, GL_FALSE, &MVP[0][0]);
		glUniformMatrix4fv(ModelMatrixID, 1, GL_FALSE, &ModelMatrix[0][0]);
		glUniformMatrix4fv(ViewMatrixID, 1, GL_FALSE, &ViewMatrix[0][0]);

		glm::vec3 lightPos = glm::vec3(4,4,4);
		glUniform3f(LightID, lightPos.x, lightPos.y, lightPos.z);

		// Bind our texture in Texture Unit 0
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, Texture);
		// Set our "myTextureSampler" sampler to user Texture Unit 0
		glUniform1i(TextureID, 0);

		// 1rst attribute buffer : vertices
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
		glVertexAttribPointer(
			0,                  // attribute
			3,                  // size
			GL_FLOAT,           // type
			GL_FALSE,           // normalized?
			0,                  // stride
			(void*)0            // array buffer offset
		);

		// 2nd attribute buffer : UVs
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ARRAY_BUFFER, uvbuffer);
		glVertexAttribPointer(
			1,                                // attribute
			2,                                // size
			GL_FLOAT,                         // type
			GL_FALSE,                         // normalized?
			0,                                // stride
			(void*)0                          // array buffer offset
		);

		// 3rd attribute buffer : normals
		glEnableVertexAttribArray(2);
		glBindBuffer(GL_ARRAY_BUFFER, normalbuffer);
		glVertexAttribPointer(
			2,                                // attribute
			3,                                // size
			GL_FLOAT,                         // type
			GL_FALSE,                         // normalized?
			0,                                // stride
			(void*)0                          // array buffer offset
		);

		// Index buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementbuffer);

		// Draw the triangles !
		glDrawElements(
			GL_TRIANGLES,      // mode
			indices.size(),    // count
			GL_UNSIGNED_SHORT,   // type
			(void*)0           // element array buffer offset
		);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);

		// Swap buffers
		glfwSwapBuffers(window);
		glfwPollEvents();

	} // Check if the ESC key was pressed or the window was closed
	while( glfwGetKey(window, GLFW_KEY_ESCAPE ) != GLFW_PRESS &&
		   glfwWindowShouldClose(window) == 0 );

	// Cleanup VBO and shader
	glDeleteBuffers(1, &vertexbuffer);
	glDeleteBuffers(1, &uvbuffer);
	glDeleteBuffers(1, &normalbuffer);
	glDeleteBuffers(1, &elementbuffer);
	glDeleteProgram(programID);
	glDeleteTextures(1, &Texture);
	glDeleteVertexArrays(1, &VertexArrayID);

	// Close OpenGL window and terminate GLFW
	glfwTerminate();

	return 0;
}

