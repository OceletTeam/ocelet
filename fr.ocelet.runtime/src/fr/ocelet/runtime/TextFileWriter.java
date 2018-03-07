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

package fr.ocelet.runtime;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import fr.ocelet.runtime.util.FileUtils;

/**
 * A class to maintain a list of files where it is possible to write lines of
 * text, one by one, through a call to printline().
 * 
 * @author Pascal Degenne, Initial contribution
 */
public class TextFileWriter {
	private static HashMap<String, PrintWriter> fmap;

	/**
	 * Constructs an initialized FileWriter
	 */
	public TextFileWriter() {
		fmap = new HashMap<String, PrintWriter>();
	}

	public static String print2file(String filename, String textline) {
		return printToFile(filename, textline);
	}

	/**
	 * Prints one line of text at the end of the specified file.
	 * 
	 * @param filename
	 *            Name (and path) of the file to be updated. If the file does
	 *            not exist, it will be created.
	 * @param textLine
	 *            Line of text to be added at the end of the file.
	 * @return Error message string if any problem occurred, or null if
	 *         everything went fine.
	 */
	public static String printToFile(String filename, String textLine) {
		if (fmap == null) {
			fmap = new HashMap<String, PrintWriter>();
		}
		String ofname = FileUtils.applyOutput(filename);

		PrintWriter pw = fmap.get(ofname);
		String errorString = null;
		if (pw == null) {
			try {
				pw = new PrintWriter(new BufferedWriter(new FileWriter(ofname,
						true)));
				fmap.put(ofname, pw);
			} catch (FileNotFoundException e) {
				errorString = "Impossible to create the file " + filename;
			} catch (IOException e) {
				errorString = "Impossible to create the file " + filename;
			}
		}
		if (pw != null) {
			pw.println(textLine);
			pw.flush();
		}
		return errorString;
	}

	/**
	 * Checks if the given filemane is already registered as a Printwriter
	 * 
	 * @param filename
	 * @return true if the corresponding file is known by fmap
	 */
	public static boolean isKnownFile(String filename) {
		String ofname = FileUtils.applyOutput(filename);
		return ((fmap != null) && fmap.containsKey(ofname));
	}

	/**
	 * Remove the given file from the list of known files.
	 * 
	 * @param filename
	 */
	public static void forget(String filename) {
		String ofname = FileUtils.applyOutput(filename);
		if (fmap == null) {
			fmap = new HashMap<String, PrintWriter>();
		}
		fmap.remove(ofname);
	}

	/**
	 * Closes every file that could have been opened during the successive calls
	 * to printline.
	 */
	public static void close() {
		for (Iterator<PrintWriter> pwit = fmap.values().iterator(); pwit
				.hasNext();) {
			pwit.next().close();
		}
	}
}
