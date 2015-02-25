package fr.ocelet.p3x.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage;
import org.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizard;

public class ImportModel extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWizard wizard = new ExternalProjectImportWizard();
		((ExternalProjectImportWizard) wizard).init(PlatformUI.getWorkbench(), null);
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.create();
		WizardProjectsImportPage iwp = (WizardProjectsImportPage)dialog.getCurrentPage();
		iwp.getCopyCheckbox().setSelection(true);
		iwp.setTitle("Import Ocelet projects");
		iwp.setMessage("Select a directory to search for existing Ocelet projects.");
		dialog.open();
		return null;
	}


}
