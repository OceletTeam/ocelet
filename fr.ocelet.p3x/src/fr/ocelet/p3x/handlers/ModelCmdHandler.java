package fr.ocelet.p3x.handlers;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.core.commands.AbstractHandler;

public abstract class ModelCmdHandler extends AbstractHandler{

	protected IProject getSelectedProject() {
		IProject selectedProject = null;

		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window != null) {
			IEditorPart editor = window.getActivePage().getActiveEditor();
			if (editor != null) {
				IResource resource = (IResource) editor.getEditorInput()
						.getAdapter(IResource.class);
				if (resource != null)
					selectedProject = resource.getProject();
			} else {
				IStructuredSelection selection = (IStructuredSelection) window
						.getSelectionService().getSelection();
				Iterator<Object> iter = selection.iterator();
				while (iter.hasNext()) {
					Object item = iter.next();
					// get current project:
					if (item instanceof IProject) {
						selectedProject = ((IProject) item);
					} else if (item instanceof IFolder) {
						selectedProject = ((IFolder) item).getProject();
					} else if (item instanceof IFile) {
						selectedProject = ((IFile) item).getProject();
					}
				}
			}
		}
		return selectedProject;
	}
	
}
