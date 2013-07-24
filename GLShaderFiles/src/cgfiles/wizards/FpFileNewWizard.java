package cgfiles.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;



/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "vp". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class FpFileNewWizard extends CgFileNewWizard {
		private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public FpFileNewWizard() {
		super();
		
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new FpFileWizardPage(
				"Create a new Fragment Program File",
				(StructuredSelection) selection);
		page.setFileName("fragment_program.fp");
		page.setTitle("New Cg Fagment File");
		page.setDescription("Create a new Fragment Program File"); 
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
		setWindowTitle("Create a new Fragment Program File");
	}
}