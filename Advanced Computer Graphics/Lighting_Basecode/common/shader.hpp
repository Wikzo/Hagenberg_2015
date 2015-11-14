#ifndef SHADER_HPP
#define SHADER_HPP

GLuint LoadShaders(const char * vertex_file_name,const char * fragment_file_name, const char* contentPath);
GLuint LoadShaders(const char * vertex_file_name, const char * geometry_file_name, const char * fragment_file_name, const char* contentPath);


class ShaderEffect
{
public:
	unsigned int programId;
	unsigned int MVPId;
	unsigned int VId;
	unsigned int MId;
	unsigned int textureSamplerId;
	unsigned int fluxId;
	unsigned int ndotl_ndotvSamplerId;
	unsigned int ndotl_ndothSamplerId;
	unsigned int ndotl_vdotlSamplerId;
	unsigned int cubeSamplerId;
	unsigned int lightPositionId;
	unsigned int specColorId;
	unsigned int shininessId;
	unsigned int metalnessId;
	unsigned int specularityId;
    unsigned int lightMatrixId;
    unsigned int constantColorId;
    unsigned int isShadowCasterId;
	unsigned int shadowMagicNumberId;
	// LOAD TEXTURE (2)
	unsigned int myNormalMapId;

	ShaderEffect(unsigned int programId);
};

#endif
