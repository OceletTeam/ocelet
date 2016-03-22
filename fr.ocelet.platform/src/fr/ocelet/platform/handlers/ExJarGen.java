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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.osgi.framework.Bundle;
/**
 * Exports an Ocelet model into a self executable jar file
 * @author Pascal Degenne - Initial contribution
 *
 */
public class ExJarGen extends ModelCmdHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IProject selectedProject = getSelectedProject();

		// Alarm if no project seem to be selected
		if (selectedProject == null) {
			MessageDialog
					.openWarning(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(), "Warning",
							"Please select an element of the project you want to export.");
			return null;
		}
		String modelName = selectedProject.getName();

		// Choose a file name and destination
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		dialog.setFilterNames(new String[] { "jar files", "All Files (*.*)" });
		dialog.setFilterExtensions(new String[] { "*.jar", "*.*" });
		dialog.setFileName(modelName.toLowerCase() + ".jar");
		String choosenfile = dialog.open();
		// In case of windows file separator, we replace them to ant
		// compatible file separator '/'
		choosenfile = choosenfile.replace("\\", "/");

		Extjarjob job = new Extjarjob(selectedProject, choosenfile);
		job.schedule();
		return null;
	}

	protected class Extjarjob extends Job {

		private IProject selectedProject;
		private String modelName;
		private String choosenfile;

		public Extjarjob(IProject selectedProject, String choosenfile) {
			super("Exporting to " + choosenfile + " ...");
			this.selectedProject = selectedProject;
			this.choosenfile = choosenfile;
			modelName = selectedProject.getName();
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			monitor.beginTask("Exporting jar for " + modelName + " ...", 100);

			monitor.worked(25);

			// Generating the ant file from a template
			try {

				IFile classpathFile = selectedProject.getFile(".classpath");
				InputStream iscp = classpathFile.getContents();
				BufferedReader brcp = new BufferedReader(new InputStreamReader(
						iscp));
				String cpline = "";
				StringBuffer cpathlist = new StringBuffer();
				StringBuffer filesets = new StringBuffer();
				while ((cpline = brcp.readLine()) != null) {
					int deb = cpline.lastIndexOf("plugins/");
					int fin = cpline.lastIndexOf(".jar");
					if ((deb > 0) && (fin > 0)) {
						String cpjar = cpline.substring(deb + 8, fin + 4);
						cpathlist.append(cpjar + " ");
						filesets.append("        <include name=\""+cpjar+"\" />\n");
					}
				}

				Bundle oceletPlatformBundle = Platform
						.getBundle("fr.ocelet.platform");
				Path antTemplate = new Path("utils/templates/ant_exjargen.xml");
				InputStream tinputStream = null;
				tinputStream = FileLocator.openStream(oceletPlatformBundle,
						antTemplate, false);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						tinputStream));
				IFile antIf = selectedProject.getFile("output/jargen.xml");
				File antFile = antIf.getRawLocation().makeAbsolute().toFile();
				antFile.createNewFile();
				FileWriter antfw = new FileWriter(antFile);
				String rline = null;
				String wline = null;

				String ocltlibsversion = Platform.getBundle("fr.ocelet.libs").getVersion().toString();
				
				while ((rline = br.readLine()) != null) {
					wline = rline.replaceAll("_MODELNAME_", modelName);
					wline = wline.replaceAll("_MAINCLASS_", "fr.ocelet.model."
							+ modelName.toLowerCase() + "." + modelName);
					wline = wline.replaceAll("_DESTFILE_", choosenfile);
					wline = wline.replaceAll("_OCLTLIBS_", "fr.ocelet.libs_"+ocltlibsversion);
					wline = wline.replaceAll("_CPATH_", cpathlist.toString());
					if (rline.contains("_JARFILES_"))
						antfw.write(filesets.toString());
					else
						antfw.write(wline
								+ System.getProperty("line.separator"));
				}
				antfw.close();

				monitor.worked(25);

				Project p = new Project(); // ant project

				// Defines ant console logger
				DefaultLogger consoleLogger = new DefaultLogger();
				MessageConsole capiBuildConsole = new MessageConsole(
						"Generating " + modelName.toLowerCase() + ".jar", null);
				ConsolePlugin.getDefault().getConsoleManager()
						.addConsoles(new IConsole[] { capiBuildConsole });
				PrintStream printStream = new PrintStream(
						capiBuildConsole.newOutputStream());
				consoleLogger.setErrorPrintStream(printStream);
				consoleLogger.setOutputPrintStream(printStream);
				consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
				p.addBuildListener(consoleLogger);

				// Executes ant target
				p.setUserProperty("ant.file", antFile.getAbsolutePath());
				p.init();
				ProjectHelper helper = ProjectHelper.getProjectHelper();
				p.addReference("ant.projectHelper", helper);
				helper.parse(p, antFile);

				monitor.worked(25);

				p.executeTarget(p.getDefaultTarget());
				
				IFile ilibsfile = selectedProject.getFile("output/libs.jar");
				File libsfile = ilibsfile.getRawLocation().makeAbsolute().toFile();
				libsfile.delete();
				antFile.delete();
				
				printStream.println("Done.");
			} catch (BuildException be) {
				be.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (CoreException cex) {
				cex.printStackTrace();
			}

			monitor.done();
			
			//
			return Status.OK_STATUS;
		}
	}

}