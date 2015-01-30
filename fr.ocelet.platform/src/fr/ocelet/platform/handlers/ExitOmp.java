package fr.ocelet.platform.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IWorkbench;

import fr.ocelet.platform.wizards.NewProjectWizard;

@SuppressWarnings("restriction")
public class ExitOmp {

	NewProjectWizard npw;

	@Execute
	public void execute(IWorkbench workbench) {
      workbench.close();
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}


}
