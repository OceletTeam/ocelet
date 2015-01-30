package fr.ocelet.platform.handlers;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import fr.ocelet.platform.dialogs.ShowGradientsDialog;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.styling.Gradient;

@SuppressWarnings("restriction")
public class ShowGradients extends ModelCmdHandler {

	@Execute
	public void execute(Shell shell) {
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
			MessageDialog.openWarning(shell, "No gradient definition", "Please select an Ocelet project.");
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
