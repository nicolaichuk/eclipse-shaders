/**
 * 
 */
package cgEditor.editors;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Martinez
 * 
 */
public class GLSLScanner extends ShaderFileScanner {

	static char escChar[] = { '\n', '\t', ' ', '.', ';', ',', '(', ')', '[', ']' };

	static final String language[] = { "std140", "state", "FRAGMENT", "VERTEX", "break", "const", "continue", "discard", "do", "else", "false", "for", "if",
			"in", "inout", "out", "return", "true", "uniform", "varying", "void", "while", "#define", "#else", "#elif",
			"#shader", "#for", "#endfor", "#include", "#endif", "#error", "#if", "#ifdef", "#ifndef", "#line", "#undef", "#pragma", "class", "enum", "typdef",
			"union", "active", "asm", "break", "case", "cast", "centroid", "common", "default", "extern", "external",
			"filter", "fixed", "flat", "foreach", "goto", "highp", "inline", "input", "interface", "invariant",
			"layout", "lowp", "mediump", "namespace", "noinline", "noperspective", "output", "packed", "partition",
			"patch", "precision", "public", "row_major", "sample", "sizeof", "smooth", "static", "struct", "subroutine",
			"superp", "switch", "template", "this", "typedef", "using", "volatile" };

	static final String types[] = { "float", "vec2", "vec3", "vec4", "sampler1D", "sampler1DShadow", "sampler2D",
			"sampler2DShadow", "sampler3D", "samplerCube", "mat2", "mat3", "mat4", "ivec2", "ivec3", "ivec4", "bvec2",
			"bvec3", "bvec4", "attribute", "bool", "gl_Color", "gl_FragCoord", "gl_FogCoord", "gl_FrontFacing",
			"gl_MultiTexCoord0", "gl_MultiTexCoord1", "gl_MultiTexCoord2", "gl_MultiTexCoord3", "gl_MultiTexCoord4",
			"gl_MultiTexCoord5", "gl_MultiTexCoord6", "gl_MultiTexCoord7", "gl_MultiTexCoord8", "gl_MultiTexCoord9",
			"gl_MultiTexCoord10", "gl_MultiTexCoord11", "gl_MultiTexCoord12", "gl_MultiTexCoord13",
			"gl_MultiTexCoord14", "gl_MultiTexCoord15", "gl_MultiTexCoord16", "gl_MultiTexCoord17",
			"gl_MultiTexCoord18", "gl_MultiTexCoord19", "gl_MultiTexCoord20", "gl_MultiTexCoord21",
			"gl_MultiTexCoord22", "gl_MultiTexCoord23", "gl_MultiTexCoord24", "gl_MultiTexCoord25",
			"gl_MultiTexCoord26", "gl_MultiTexCoord27", "gl_MultiTexCoord28", "gl_MultiTexCoord29",
			"gl_MultiTexCoord30", "gl_MultiTexCoord31", "gl_SecondaryColor", "gl_Normal", "gl_Vertex", "gl_PointSize",
			"gl_Position", "gl_ClipVertex", "gl_FragColor", "gl_FragDepth", "gl_FrontColor", "gl_BackColor",
			"gl_FrontSecondaryColor", "gl_BackSecondaryColor", "gl_TexCoord", "gl_FogFragCoord", "dmat2", "dmat2x2",
			"dmat2x3", "dmat2x4", "dmat3", "dmat3x2", "dmat3x3", "dmat3x4", "dmat4", "dmat4x2", "dmat4x3", "dmat4x4",
			"double", "dvec2", "dvec3", "dvec4", "fvec2", "fvec3", "fvec4", "half", "hvec2", "hvec3", "hvec4", "int",
			"isampler1D", "isampler1DArray", "isampler2D", "isampler2DArray", "isampler2DMS", "isampler2DMSArray",
			"isampler2DRect", "isampler3D", "isamplerBuffer", "isamplerCube", "isamplerCubeArray", "long", "mat2x2",
			"mat2x3", "mat2x4", "mat3x2", "mat3x3", "mat3x4", "mat4x2", "mat4x3", "mat4x4", "sampler1DArray",
			"sampler1DArrayShadow", "sampler2DArray", "sampler2DArrayShadow", "sampler2DMS", "sampler2DMSArray",
			"sampler2DRect", "sampler2DRectShadow", "sampler3DRect", "samplerBuffer", "samplerCubeArray",
			"samplerCubeArrayShadow", "samplerCubeShadow", "short", "uint", "unsigned", "usampler1D", "usampler1DArray",
			"usampler2D", "usampler2DArray", "usampler2DMS", "usampler2DMSArray", "usampler2DRect", "usampler3D",
			"usamplerBuffer", "usamplerCube", "usamplerCubeArray", "uvec2", "uvec3", "uvec4" };

	static final String functions[] = { "radians", "degrees", "sin", "cos", "tan", "asin", "acos", "atan", "pow",
			"exp2", "log2", "sqrt", "inversesqrt", "abs", "sign", "floor", "cei", "fract", "mod", "min", "max", "clamp",
			"mix", "step", "smoothstep", "length", "distance", "dot", "cross", "normalize", "ftransform", "faceforward",
			"reflect", "matrixcompmult", "lessThan", "lessThanEqual", "greaterThan", "greaterThanEqual", "equal",
			"notEqual", "any", "all", "not", "texture1D", "texture1DProj", "texture1DLod", "texture1DProjLod",
			"texture2D", "texture2DProj", "texture2DLod", "texture2DProjLod", "texture3D", "texture3DProj",
			"texture3DLod", "texture3DProjLod", "textureCube", "textureCubeLod", "shadow1D", "shadow1DProj",
			"shadow1DLod", "shadow1DProjLod", "shadow2D", "shadow2DProj", "shadow2DLod", "shadow2DProjLod", "dFdx",
			"dFdy", "fwidth", "noise1", "noise2", "noise3", "noise4", "ceil", "exp", "log", "refract", "texelFetch",
			"texelFetchOffset", "textureGrad", "textureGradOffset", "textureLod", "textureLodOffset", "textureOffset",
			"textureProj", "textureProjGrad", "textureProjGradOffset", "textureProjLod", "textureProjLodOffset",
			"textureProjOffset", "textureSize" };

	static final String semantics[] = { "gl_ModelViewMatrix", "gl_ProjectionMatrix", "gl_ModelViewProjectionMatrix",
			"gl_NormalMatrix", "gl_TextureMatrix", "gl_NormalScale", "gl_DepthRangeParameters", "gl_DepthRange",
			"gl_ClipPlane", "gl_PointParameters", "gl_Point", "gl_MaterialParameters", "gl_FrontMaterial",
			"gl_BackMaterial", "gl_LightSourceParameters", "gl_LightSource", "gl_LightModelParamters", "gl_LightModel",
			"gl_LightModelProducts", "gl_FrontLightModelProduct", "gl_BackLightModelProduct", "gl_LightProducts",
			"gl_FrontLightProduct", "gl_BackLightProduct", "gl_TextureEnvColor", "gl_EyePlaneS", "gl_EyePlaneT",
			"gl_EyePlaneR", "gl_EyePlaneQ", "gl_ObjectPlaneS", "gl_ObjectPlaneT", "gl_ObjectPlaneR", "gl_ObjectPlaneQ",
			"gl_FogParameters", "gl_Fog", "gl_MaxLights", "gl_MaxClipPlanes", "gl_MaxTextureUnits",
			"gl_MaxTextureCoords", "gl_MaxVertexAttribs", "gl_MaxVertexUniformComponents", "gl_MaxVaryingFloats",
			"gl_MaxVectexTextureImageUnits", "gl_MaxTextureImageUnits", "gl_MaxFragmentUniformComponents",
			"gl_MaxCombinedTextureImageUnits", "gl_ClipDistance", "gl_FragData", "gl_GlobalInvocationID", "gl_in",
			"gl_InstanceID", "gl_InvocationID", "gl_Layer", "gl_LightModelParameters", "gl_LocalInvocationID",
			"gl_LocalInvocationIndex", "gl_MaxCombinedTextureImageUnits", "gl_MaxDrawBuffers", "gl_MaxPatchVertices",
			"gl_MaxVertexTextureImageUnits", "gl_ModelViewMatrixInverse", "gl_ModelViewMatrixInverseTranspose",
			"gl_ModelViewMatrixTranspose", "gl_ModelViewProjectionMatrixInverse",
			"gl_ModelViewProjectionMatrixInverseTranspose", "gl_ModelViewProjectionMatrixTranspose", "gl_NumSamples",
			"gl_NumWorkGroups", "gl_out", "gl_PatchVerticesIn", "gl_PrimitiveID", "gl_PrimitiveIDIn", "gl_PointCoord",
			"gl_ProjectionMatrixInverse", "gl_ProjectionMatrixInverseTranspose", "gl_ProjectionMatrixTranspose",
			"gl_SampleID", "gl_SampleMaskIn", "gl_SamplePosition", "gl_TessCoord", "gl_TessLevelInner",
			"gl_TessLevelOuter", "gl_TextureMatrixInverse", "gl_TextureMatrixInverseTranspose",
			"gl_TextureMatrixTranspose", " gl_ViewportIndex", "gl_WorkGroupID", "gl_WorkGroupSize" };


	private static String[] load(final Properties property, final String name, final String[] defVal) {
		final String str = property.getProperty(name);
		String[] ret = str != null ? str.split(",") : defVal;
		for(int i = 0; i < ret.length; i++) {
			ret[i] = ret[i].trim();
		}
		return ret;
	}
	
	public GLSLScanner() {
		super();
		
		final Properties property = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream("/cgEditor/editors/config.properties");
		if ( is != null ) {
			try {
				property.load(is);
			} catch (final IOException e) {
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		super.language = load(property, "language", GLSLScanner.language);
		super.types = load(property, "types", GLSLScanner.types);
		super.functions = load(property, "functions", GLSLScanner.functions);
		super.semantics = load(property, "semantics", GLSLScanner.semantics);
	}

}
