/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
*
*  This software is a domain specific programming language dedicated to writing
*  spatially explicit models and performing spatial dynamics simulations.
*
*  This software is governed by the CeCILL license under French law and
*  abiding by the rules of distribution of free software.  You can  use,
*  modify and/ or redistribute the software under the terms of the CeCILL
*  license as circulated by CEA, CNRS and INRIA at the following URL
*  "http://www.cecill.info".
*  As a counterpart to the access to the source code and  rights to copy,
*  modify and redistribute granted by the license, users are provided only
*  with a limited warranty  and the software's author,  the holder of the
*  economic rights,  and the successive licensors  have only limited
*  liability.
*  The fact that you are presently reading this means that you have had
*  knowledge of the CeCILL license and that you accept its terms.
*/

package fr.ocelet.platform.handlers;

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

/**
 * Common class to all action handlers of the OMP.
 * @author Pascal Degenne - Initial contribution
 *
 */
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
