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

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import fr.ocelet.platform.dialogs.NewOcltFileDialog;

/**
 * Creates a new Ocelet file and opens it in an editor
 * 
 * @author Pascal Degenne - Initial contribution
 */
public class NewOcltFile extends ModelCmdHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		IProject selectedProject = getSelectedProject();
		NewOcltFileDialog nofd = new NewOcltFileDialog(shell);
		nofd.create();

		if (nofd.open() == Window.OK) {
			IFile ocltf = selectedProject.getFile("oclt/" + nofd.getFileName());
			File ocltFile = ocltf.getRawLocation().makeAbsolute().toFile();
			try {
				ocltFile.createNewFile();
				selectedProject.refreshLocal(IResource.DEPTH_INFINITE, null);
				IFileStore fileStore = EFS.getLocalFileSystem().getStore(
						ocltFile.toURI());
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IDE.openEditorOnFileStore(page, fileStore);
			} catch (IOException e) {
				System.err.println("Failed to create the file "
						+ nofd.getFileName());
			} catch (CoreException e) {
				System.err.println("Failed to open the file "
						+ nofd.getFileName());
			}
		}
		return null;
	}

}
