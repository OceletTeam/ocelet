/*
 *  Ocelet spatial modelling language.   www.ocelet.org
 *  Copyright Cirad 2010-2016
 *
 *  This software is a domain specific programming language dedicated to writing
 *  spatially explicit models and performing spatial dynamics simulations.
 *
 *  This software is governed by the CeCILL license under French law and
 *  abiding by the rules of distribution of free software.  You can  use,
 *  modify and/ or redistribute the software under the terms of the CeCILL
 *  license as circulated by CEA, CNRS and INRIA at the following URL
 *  "http://www.cecill.info".
 *  As a counterpart to the access to the source code and  rights to copy,
 *  modify and redistribute granted by the license, users are provided only
 *  with a limited warranty  and the software's author,  the holder of the
 *  economic rights,  and the successive licensors  have only limited
 *  liability.
 *  The fact that you are presently reading this means that you have had
 *  knowledge of the CeCILL license and that you accept its terms.
 */

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

/**
 * Displays the color gradients defined in the config/gradient.ocg file.
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */
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
