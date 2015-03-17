package fr.ocelet.platform.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Saves and compiles the content of the active editor.
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */

public class SaveModel extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IEditorPart editor = window.getActivePage().getActiveEditor();
		if (editor != null)
			editor.doSave(null);
		return null;
	}

}
