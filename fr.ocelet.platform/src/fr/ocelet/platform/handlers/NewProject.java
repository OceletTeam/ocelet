package fr.ocelet.platform.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import fr.ocelet.platform.wizards.NewProjectWizard;
import fr.ocelet.platform.wizards.models.ProjectModel;

@SuppressWarnings({ "restriction" })
public class NewProject {

	NewProjectWizard npw;

	@Execute
	public void execute() {

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		npw = new NewProjectWizard();

		WizardDialog refWizard1Dialog = new WizardDialog(shell, npw);
		refWizard1Dialog.create();

		if (refWizard1Dialog.open() == Window.OK) {
			ProjectModel pm = npw.getProjectModel();
			if (pm != null)
				pm.createModel();
		}
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
