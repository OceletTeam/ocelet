package fr.ocelet.p3x.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import fr.ocelet.p3x.wizards.NewProjectWizard;
import fr.ocelet.p3x.wizards.models.ProjectModel;

public class NewProject extends AbstractHandler {

	NewProjectWizard npw;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		npw = new NewProjectWizard();

		WizardDialog refWizard1Dialog = new WizardDialog(shell, npw);
		refWizard1Dialog.create();

		if (refWizard1Dialog.open() == Window.OK) {
			ProjectModel pm = npw.getProjectModel();
			if (pm != null)
				pm.createModel();
		}
	  return null;
	}

}
