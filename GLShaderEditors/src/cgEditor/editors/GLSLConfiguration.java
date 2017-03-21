/**
 * 
 */
package cgEditor.editors;

import cgEditor.preferences.PreferenceConstants;

/**
 * @author Martinez
 *
 */
public class GLSLConfiguration extends ShaderFileConfiguration {

	/**
	 * 
	 */
	public GLSLConfiguration() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected ShaderFileScanner getTagScanner() {
		if(scanner ==null)
		{
			scanner = new GLSLScanner();
			scanner.setShaderRules();
			scanner.setDefaultReturnToken(TokenManager.getToken(PreferenceConstants.DEFAULTSTRING));
		}
		return scanner;
	}
}	


