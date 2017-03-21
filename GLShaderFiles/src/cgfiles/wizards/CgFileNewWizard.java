/**
 * 
 */
package cgfiles.wizards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ISetSelectionTarget;

import cgfiles.CgfilesPlugin;

/**
 * @author Martinez
 *
 */
public  class CgFileNewWizard extends Wizard implements INewWizard {
	protected WizardNewFileCreationPage page;

	
	/**
	 * Constructor for SampleNewWizard.
	 */
	public CgFileNewWizard() {
		super();
		setNeedsProgressMonitor(true);
		
	}

	/* (non-Javadoc)
     * Method declared on BasicNewResourceWizard.
     */
    protected void initializeDefaultPageImageDescriptor() {
       ImageDescriptor desc = CgfilesPlugin.getImageDescriptor("icons/cgbig.png");//$NON-NLS-1$
	   setDefaultPageImageDescriptor(desc);
    }
    
	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		IFile file = page.createNewFile();
		if (file == null)
			return false;

		IWorkbenchWindow dw = CgfilesPlugin.getDefault().getWorkbench()
				.getActiveWorkbenchWindow();
		selectAndReveal(file, dw);

		// Open editor on new file.

		if (dw != null) {
			IWorkbenchPage page = dw.getActivePage();
			if (page != null) {
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	/**
	 * 
	 * @param resource
	 * @param window
	 */
	public void selectAndReveal(IResource resource, IWorkbenchWindow window) {
		// validate the input
		if (window == null || resource == null)
			return;
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return;

		// get all the view and editor parts
		List<IWorkbenchPart> parts = new ArrayList<IWorkbenchPart>();
		IWorkbenchPartReference refs[] = page.getViewReferences();
		for (int i = 0; i < refs.length; i++) {
			IWorkbenchPart part = refs[i].getPart(false);
			if (part != null)
				parts.add(part);
		}
		refs = page.getEditorReferences();
		for (int i = 0; i < refs.length; i++) {
			if (refs[i].getPart(false) != null)
				parts.add(refs[i].getPart(false));
		}

		final ISelection selection = new StructuredSelection(resource);
		Iterator<IWorkbenchPart> itr = parts.iterator();
		while (itr.hasNext()) {
			IWorkbenchPart part = (IWorkbenchPart) itr.next();

			// get the part's ISetSelectionTarget implementation
			ISetSelectionTarget target = null;
			if (part instanceof ISetSelectionTarget)
				target = (ISetSelectionTarget) part;
			else
				target = part
						.getAdapter(ISetSelectionTarget.class);

			if (target != null) {
				// select and reveal resource
				final ISetSelectionTarget finalTarget = target;
				window.getShell().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						finalTarget.selectReveal(selection);
					}
				});
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		initializeDefaultPageImageDescriptor();
	}

}
