package fr.ocelet.platform.handlers;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import fr.ocelet.platform.dialogs.ShowGradientsDialog;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.styling.Gradient;

public class ShowGradients extends ModelCmdHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		IProject selectedProject = getSelectedProject();
		if (selectedProject != null)
			try {
				IPath path = selectedProject.getLocation();
				String projectbasedir = path.toString();
				KeyMap<String, Gradient> kmg = Gradient.readGradients(null,
						projectbasedir + "/config/gradients.ocg");
				ShowGradientsDialog sgd = new ShowGradientsDialog(shell, kmg,
						selectedProject.getName());
				sgd.create();
				sgd.getShell().pack();
				sgd.open();
			} catch (IOException ioe) {
				MessageDialog
						.openWarning(
								shell,
								"No gradient definition",
								"The gradient definition file (config/gradients.ocg) was not found for this project.");
			}
		else
			MessageDialog.openWarning(shell, "No gradient definition",
					"Please select an Ocelet project.");
		return null;
	}
}
