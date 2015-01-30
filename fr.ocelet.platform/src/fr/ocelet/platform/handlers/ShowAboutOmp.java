package fr.ocelet.platform.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import fr.ocelet.platform.dialogs.AboutOmpDialog;

@SuppressWarnings("restriction")
public class ShowAboutOmp extends ModelCmdHandler {

	@Execute
	public void execute(Shell shell) {
				AboutOmpDialog sgd = new AboutOmpDialog(shell);
				sgd.create();
				sgd.getShell().pack();
				sgd.open();
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
