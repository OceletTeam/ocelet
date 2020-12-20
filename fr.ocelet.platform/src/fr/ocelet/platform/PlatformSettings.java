/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2018
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

package fr.ocelet.platform;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;

/**
 * General version and debug settings of the Ocelet Modelling Platform
 *
 * @author Pascal Degenne - Initial contribution
 */
public class PlatformSettings {

	public final static String version = "Sulfur : 2.1.0 beta";

	public final static int NORMAL = 0;
	public final static int VERBOSE = 1;
	public final static int DEBUG = 2;

	public static int msgLevel = DEBUG;

	public static int getMsgLevel() {
		return msgLevel;
	}

	public static void setMsgLevel(int msgLevel) {
		PlatformSettings.msgLevel = msgLevel;
	}

	public PlatformSettings() {
		msgLevel = NORMAL;
	}

	/**
	 * Generates the .classpath file of an Ocelet project. This generation
	 * process adapts the version of included jar files to the ones released
	 * with the OMP version being executed.
	 */
	public static void generateClasspath(IProject selectedProject) {

		// Prepare the classpath file content in a String
		StringBuffer spsb = new StringBuffer();
		spsb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		spsb.append("<classpath>\n");
		spsb.append(
				"  <classpathentry exported=\"true\" kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\n");
		spsb.append("  <classpathentry kind=\"src\" path=\"src\"/>\n");
		spsb.append("  <classpathentry kind=\"src\" path=\"oclt\"/>\n");
		spsb.append("  <classpathentry kind=\"output\" path=\"bin\"/>\n");

		final String prefix = "  <classpathentry kind=\"lib\" path=\"../../plugins/";
		final String suffix = ".jar\"/>\n";

		spsb.append(prefix + getVersioned("org.eclipse.xtext.xbase.lib") + suffix);
		spsb.append(prefix + getVersioned("com.google.guava") + suffix);
		spsb.append(prefix + getVersioned("fr.ocelet.datafacer") + suffix);
		spsb.append(prefix + getVersioned("fr.ocelet.runtime") + suffix);
		spsb.append(prefix + getVersioned("fr.ocelet") + suffix);

		try {
			Files.walk(Paths.get("plugins/" + getVersioned("fr.ocelet.libs"))).forEach(filePath -> {
				if (Files.isRegularFile(filePath) && (filePath.toString().endsWith("jar"))) {
					spsb.append("  <classpathentry kind=\"lib\" path=\"../../" + filePath + "\"/>\n");
				}
			});
		} catch (IOException e1) {
			System.err.println(
					"Could not read the fr.ocelet.libs bundle, Ocelet may not be installed correctly or has been corrupted.");
		}
		spsb.append("</classpath>");

		// Save the classpath content into the .classpath file
		IFile cpfile = selectedProject.getFile(".classpath");
		try {
			if (cpfile.exists())
				cpfile.delete(true, null);
			InputStream istr = new ByteArrayInputStream(spsb.toString().getBytes(Charset.forName("UTF-8")));
			cpfile.create(istr, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private static String getVersioned(String plugin) {
		return plugin + "_" + Platform.getBundle(plugin).getVersion().toString();
	}
}
