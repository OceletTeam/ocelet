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

package fr.ocelet.platform.wizards.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Bundle;

import fr.ocelet.platform.PlatformSettings;

/**
 * Ocelet project description and creation class
 * 
 * @author Pascal Degenne - Initial contribution
 */
public abstract class ProjectModel {

	/**
	 * Creates a new project with the appropriate default settings and folder
	 * structures.
	 */
	public void createModel() {

		String errmsg = "";
		IProjectDescription newDescription;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IProject newProject = root.getProject(getModelName());

		try {
			errmsg = "creating the project";
			IProgressMonitor progressMonitor = new NullProgressMonitor();
			newProject.create(progressMonitor);
			newProject.open(progressMonitor);

			errmsg = "creating the .project file";
			newDescription = newProject.getDescription();

			// natures:
			String[] natures = newDescription.getNatureIds();
			String[] newNatures = new String[natures.length + 2];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[newNatures.length - 2] = "org.eclipse.jdt.core.javanature";
			newNatures[newNatures.length - 1] = "org.eclipse.xtext.ui.shared.xtextNature";
			newDescription.setNatureIds(newNatures);

			// comment:
			newDescription
					.setComment("Ocelet Project created by the Ocelet Modelling Platform v"
							+ PlatformSettings.version);

			// now update the description of the new project:
			newProject.setDescription(newDescription, null);

			// Create the folders
			errmsg = "creating the folders structure";
			IFolder srcFolder = (IFolder) newProject.getFolder("src");
			IFolder binFolder = (IFolder) newProject.getFolder("bin");
			IFolder datFolder = (IFolder) newProject.getFolder("data");
			IFolder ocltFolder = (IFolder) newProject.getFolder("oclt");
			IFolder cfgFolder = (IFolder) newProject.getFolder("config");
			IFolder outputFolder = (IFolder) newProject.getFolder("output");
			binFolder.create(true, true, null);
			datFolder.create(true, true, null);
			ocltFolder.create(true, true, null);
			cfgFolder.create(true, true, null);
			outputFolder.create(true, true, null);
			srcFolder.create(true, true, null);

			// Create the .classpath file
			PlatformSettings.generateClasspath(newProject);

			// Add a dummy minimal ocelet model
			Bundle oceletPlatformBundle = Platform.getBundle("fr.ocelet.platform");
			errmsg = "adding a template model";
			Path modelTemplate = new Path(
					"utils/templates/DefaultMinimalScenario.txt");
			InputStream tinputStream = null;
			tinputStream = FileLocator.openStream(oceletPlatformBundle,
					modelTemplate, false);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					tinputStream));
			IFile ocltIf = newProject.getFile("oclt/" + getModelName()
					+ ".oclt");
			File ocltFile = ocltIf.getRawLocation().makeAbsolute().toFile();
			ocltFile.createNewFile();
			FileWriter ocltfw = new FileWriter(ocltFile);
			String rline = null;
			String wline = null;
			while ((rline = br.readLine()) != null) {
				wline = rline.replaceAll("_MODELNAME_", getModelName());
				ocltfw.write(wline + System.getProperty("line.separator"));
			}
			ocltfw.close();

			// Add a gradient.ocg into config
			errmsg = "adding the default gradient.ocg file";
			Path gradientPath = new Path("utils/gradients.ocg");
			InputStream ginputStream = null;
			ginputStream = FileLocator.openStream(oceletPlatformBundle,
					gradientPath, false);
			BufferedReader gbr = new BufferedReader(new InputStreamReader(
					ginputStream));
			IFile grdf = newProject.getFile("config/gradients.ocg");
			grdf.create(ginputStream, true, null);

			newProject.refreshLocal(IResource.DEPTH_INFINITE, null);

			IFileStore fileStore = EFS.getLocalFileSystem().getStore(
					ocltFile.toURI());
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			IEditorPart iep=  IDE.openEditorOnFileStore(page, fileStore);
			iep.doSave(null);
			

		} catch (CoreException e) {
			if (e.getMessage().contains("already exists")) {
				Shell shell = PlatformUI.getWorkbench().getDisplay()
						.getActiveShell();
				MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING
						| SWT.OK);
				dialog.setText("Project could not be created");
				dialog.setMessage("A project named " + getModelName()
						+ " already exists in the workspace.");
				dialog.open();
			} else if (e.getMessage().contains("invalid name")) {
				Shell shell = PlatformUI.getWorkbench().getDisplay()
						.getActiveShell();
				MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING
						| SWT.OK);
				dialog.setText("Project could not be created");
				dialog.setMessage("The text '" + getModelName()
						+ "' is not a valid project name.");
				dialog.open();
			} else {
				System.err.println("Failed to create the Ocelet project "
						+ modelName + ".");
				if (PlatformSettings.msgLevel >= PlatformSettings.VERBOSE)
					System.err.println("While " + errmsg + ".");
				if (PlatformSettings.msgLevel >= PlatformSettings.DEBUG)
					e.printStackTrace();
			}
		} catch (IOException e1) {
			System.err.println("Failed to create the Ocelet project "
					+ modelName + ".");
			if (PlatformSettings.msgLevel >= PlatformSettings.VERBOSE)
				System.err.println("While " + errmsg + ".");
			if (PlatformSettings.msgLevel >= PlatformSettings.DEBUG)
				e1.printStackTrace();
		}
	}

	public void createModelFromTemplate(IProject project, String modelName,
			String templatePath) {

	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	private String modelName = "MissingModelName";
}
