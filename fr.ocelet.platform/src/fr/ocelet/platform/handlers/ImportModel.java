package fr.ocelet.platform.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import fr.ocelet.platform.wizards.ImportProjectWizard;
import fr.ocelet.platform.wizards.ImportProjectWizardPage;

@SuppressWarnings("restriction")
public class ImportModel extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IWizard wizard = new ImportProjectWizard();
		((ImportProjectWizard) wizard).init(PlatformUI.getWorkbench(), null);
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.create();
		ImportProjectWizardPage iwp = (ImportProjectWizardPage) dialog
				.getCurrentPage();
		iwp.setTitle("Import Ocelet projects");
		iwp.setMessage("Select a directory to search for existing Ocelet projects.");
		dialog.open();
		return null;
	}

}
