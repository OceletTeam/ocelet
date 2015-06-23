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
