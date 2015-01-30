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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.osgi.framework.Bundle;

public class ExJarGen extends ModelCmdHandler {

	@Execute
	public void execute(IWorkbench workbench) {

		IProject selectedProject = getSelectedProject();

		// Alarm if no project seem to be selected
		if (selectedProject == null) {
			MessageDialog
					.openWarning(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(), "Warning",
							"Please select an element of the project you want to export.");
			return;
		}

		// Generating the ant file from a template
		try {

			IFile classpathFile = selectedProject.getFile(".classpath");
			InputStream iscp = classpathFile.getContents();
			BufferedReader brcp = new BufferedReader(
					new InputStreamReader(iscp));
			String cpline = "";
			StringBuffer cpathlist = new StringBuffer();
			StringBuffer filesets = new StringBuffer();
			while ((cpline = brcp.readLine()) != null) {
				int deb = cpline.lastIndexOf("plugins/");
				int fin = cpline.lastIndexOf(".jar");
				if ((deb > 0) && (fin > 0)) {
					String cpjar = cpline.substring(deb + 8, fin + 4);
					cpathlist.append(cpjar + " ");
					filesets.append("      <zipfileset src=\"${plugins.dir}/"
							+ cpjar + "\" excludes=\"META-INF/*.SF\"/>\n");
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
			String modelName = selectedProject.getName();

			while ((rline = br.readLine()) != null) {
				wline = rline.replaceAll("_MODELNAME_", modelName);
				wline = wline.replaceAll("_MAINCLASS_", "fr.ocelet.model."
						+ modelName.toLowerCase() + "." + modelName);
				wline = wline.replaceAll("_DESTFILE_", modelName.toLowerCase()
						+ ".jar");
				wline = wline.replaceAll("_CPATH_", cpathlist.toString());
				if (rline.contains("_JARFILES_"))
					antfw.write(filesets.toString());
				else
					antfw.write(wline + System.getProperty("line.separator"));
			}
			antfw.close();

			Project p = new Project(); // ant project
			// p.setInputHandler(new PluginInputHandler());

			// Defines ant console logger
			DefaultLogger consoleLogger = new DefaultLogger();
			MessageConsole capiBuildConsole = new MessageConsole("Generating "+modelName.toLowerCase()+ ".jar",
					null);
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
			p.executeTarget(p.getDefaultTarget());
			printStream.println("Done.");
		} catch (BuildException be) {
			be.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (CoreException cex) {
			cex.printStackTrace();
		}
	}

}