package fr.ocelet.platform.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Saves and compiles the content of the active editor.
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("restriction")
public class SaveModel {

	@Execute
	public void execute(IWorkbench workbench) {
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IEditorPart editor = window.getActivePage().getActiveEditor();
		if (editor != null) editor.doSave(null);
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
