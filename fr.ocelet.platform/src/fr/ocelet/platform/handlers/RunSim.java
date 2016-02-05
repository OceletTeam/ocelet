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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import fr.ocelet.platform.PlatformSettings;
import fr.ocelet.platform.launching.OceletDebugEventListener;

/**
 * Launches the execution of the selected model.
 * @author Pascal Degenne - Initial contribution
 *
 */
public class RunSim extends ModelCmdHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IProject selectedProject = getSelectedProject();

		// Alarm if no project seem to be selected
		if (selectedProject == null) {
			MessageDialog
					.openWarning(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(), "Warning",
							"Please select an element of the project you want to simulate.");
			return null;
		}

		/*
		 * Bug issue #48 : We have to locate the java source file that has the
		 * same name as the project, then pick up the folder name between "src/"
		 * and that java file and use it as being the package name.
		 */
		String packg = "fr/ocelet/model/"
				+ selectedProject.getName().toLowerCase();
		if (!selectedProject.getFolder("src/" + packg)
				.getFile(selectedProject.getName() + ".java").exists()) {
			MessageDialog.openWarning(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "Warning",
					"Java entry function (main) not found in the \"src\" folder of \""
							+ selectedProject.getName() + "\".\n");
			return null;
		}

		// Generate a launch configuration file
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager
				.getLaunchConfigurationType("org.eclipse.jdt.launching.localJavaApplication");

		ILaunchConfiguration[] configurations = null;
		ILaunchConfigurationWorkingCopy workingCopy = null;
		try {
			configurations = manager.getLaunchConfigurations(type);
		} catch (CoreException e1) {
			if (PlatformSettings.msgLevel >= PlatformSettings.VERBOSE)
				System.err
						.println("An error occured while preparing a launch configuration for this project");
			if (PlatformSettings.msgLevel >= PlatformSettings.DEBUG)
				e1.printStackTrace();
		}
		for (int i = 0; i < configurations.length; i++) {
			ILaunchConfiguration configuration = configurations[i];
			if (configuration.getName().equals(selectedProject.getName())) {
				try {
					configuration.delete();
				} catch (CoreException e) {
					if (PlatformSettings.msgLevel >= PlatformSettings.VERBOSE)
						System.err
								.println("An error occured while trying to remove an old launch configuration for this project");
					if (PlatformSettings.msgLevel >= PlatformSettings.DEBUG)
						e.printStackTrace();
				}
				break;
			}
		}

		try {
			workingCopy = type.newInstance(null, selectedProject.getName());

			workingCopy.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					selectedProject.getName());
			workingCopy.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
					packg + "/" + selectedProject.getName());
			workingCopy.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
					"");

			ILaunchConfiguration configuration = null;
			configuration = workingCopy.doSave();

			// Listener that will be notified when the simulation
			// has terminated, and will refresh the project view
			// (and the output folder)
	        DebugPlugin dplug = DebugPlugin.getDefault();
	        dplug.addDebugEventListener(new OceletDebugEventListener(selectedProject));

            // Run the simulation asynchronously
	        DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
			
		} catch (CoreException e) {
			if (PlatformSettings.msgLevel >= PlatformSettings.VERBOSE)
				System.err
						.println("An error occured while trying to create a launch configuration for this project");
			if (PlatformSettings.msgLevel >= PlatformSettings.DEBUG)
				e.printStackTrace();
		}
		return null;
	}

}