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
