package fr.ocelet.platform.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import fr.ocelet.platform.dialogs.AboutOmpDialog;

public class ShowAboutOmp extends ModelCmdHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		AboutOmpDialog sgd = new AboutOmpDialog(shell);
		sgd.create();
		sgd.getShell().pack();
		sgd.open();
		return null;
	}

}
