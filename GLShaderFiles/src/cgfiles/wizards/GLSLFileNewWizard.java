package cgfiles.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import cgfiles.CgfilesPlugin;



/**
 * This is the GLSL creation file wizard 
 */

public class GLSLFileNewWizard extends CgFileNewWizard {
		private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public GLSLFileNewWizard() {
		super();
		
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new GLSLFileWizardPage(
				"GLSL Program File",
				(StructuredSelection) selection);
		page.setFileName("shader.glsl");
		page.setTitle("New GLSL File");
		page.setDescription("Create a new GLSL Program File"); 
		addPage(page);
	}

	
	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		this.selection = selection;
		setWindowTitle("Create a new GLSL Program File");
	}
	
	/* (non-Javadoc)
     * Method declared on BasicNewResourceWizard.
     */
    protected void initializeDefaultPageImageDescriptor() {
       ImageDescriptor desc = CgfilesPlugin.getImageDescriptor("icons/openglbig.png");//$NON-NLS-1$
	   setDefaultPageImageDescriptor(desc);
    }
}