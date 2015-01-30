package fr.ocelet.platform.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage;
import org.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizard;

@SuppressWarnings("restriction")
public class ImportModel {

	@Execute
	public void execute(IWorkbench workbench) {
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWizard wizard = new ExternalProjectImportWizard();
		((ExternalProjectImportWizard) wizard).init(workbench, null);
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.create();
		WizardProjectsImportPage iwp = (WizardProjectsImportPage)dialog.getCurrentPage();
		iwp.getCopyCheckbox().setSelection(true);
		iwp.setTitle("Import Ocelet projects");
		iwp.setMessage("Select a directory to search for existing Ocelet projects.");
		dialog.open();
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
