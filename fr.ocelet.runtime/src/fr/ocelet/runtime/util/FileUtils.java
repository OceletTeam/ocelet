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

package fr.ocelet.runtime.util;

import java.io.File;

public class FileUtils {

	/**
	 * An utility method to apply the output dir and save behavior :
	 * 
	 * If no path is provided, output/ is added
	 * If the target directory doesn't exist, it is created
	 * If the new directory can't be created, replace it by output/ (and print a warning message)
	 * If the given path is valid and already exists, just use it
	 * 
	 * @author Pascal Degenne - Initial contribution
	 *
	 */
	public static String applyOutput(String filename) {

		String justfile = "";
		String path = "";
		String wrkname = filename;

		Integer xlastsep = filename.lastIndexOf(File.separator);
		if ((xlastsep < 0) && (filename.lastIndexOf("/") >= 0)) {
			wrkname = filename.replace("/", File.separator);
			xlastsep = wrkname.lastIndexOf(File.separator);
		}
		if (xlastsep < 0) {
			justfile = wrkname;
			path = "output" + File.separator;
		} else {
			justfile = wrkname.substring(xlastsep + 1);
			path = wrkname.substring(0, xlastsep + 1);
		}

		File fpath = new File(path);
		fpath = new File(path);
		if ((!fpath.exists()) && (!fpath.mkdirs())) {
			path = "output" + File.separator;
			System.err.println("Sorry, impossible to create '" + filename
					+ "', using '" + path + justfile + "' instead.");
		}
		return path + justfile;
	}

}
