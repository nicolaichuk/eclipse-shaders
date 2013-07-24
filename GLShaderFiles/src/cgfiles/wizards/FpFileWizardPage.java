/**
 * 
 */
package cgfiles.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * @author Martinez
 *
 */
public class FpFileWizardPage extends WizardNewFileCreationPage {

	/**
	 * @param pageName
	 * @param selection
	 */
	public FpFileWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getNewFileLabel() {
		// TODO Auto-generated method stub
		return "Cg Fagment File (.fp)";
	}

	@Override
	protected boolean validatePage() {
		// TODO Auto-generated method stub
		boolean validate =  super.validatePage();
		
		if(validate)
		{
			if(!getFileName().endsWith(".fp"))
			{
				validate = false;
				setErrorMessage("The file must have .fp extension");				
			}
			
		}
		
		return validate;
	}
	
	
	
}
