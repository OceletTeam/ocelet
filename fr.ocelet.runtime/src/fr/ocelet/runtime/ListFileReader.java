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

package fr.ocelet.runtime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import fr.ocelet.runtime.ocltypes.List;

/**
 * Legacy class initially used to read csv files.
 * We now have a datafacer for reading and writing csv files.
 * This class is not useful any more
 * @author Pascal Degenne - Initial contribution
 * @deprecated
 */
public class ListFileReader {

	private final String ERR_HEADER = "ListFileReader: ";
	private final static String DEFAULT_SEPARATOR = ";";
	protected BufferedReader reader;
	protected String currentLine;
	protected String separator;

	public ListFileReader(String fileName) {
		setFileName(fileName);
	}

	public String readLine() {
		String rline = "";
		try {
			rline = reader.readLine();
		} catch (IOException e) {
			System.out.println(ERR_HEADER
					+ "An error occured while reading the file.");
		}
		return rline;
	}

	public static <T> List<T> readFromCsvFile(String filename) {
		return readFromCsvFile(filename, DEFAULT_SEPARATOR);
	}

	public static <T> List<T> readFromCsvFile(String filename, String sep) {
		String[] cols;
		List<T> list = new List<T>();
		ListFileReader lfr = new ListFileReader(filename);
		String line = lfr.readLine();
		cols = line.split(sep);
		for (String s: cols){
			// TODO to be finished ...
		}
		return null;
	}

	protected void setFileName(String fileName) {
		try {
			reader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println(ERR_HEADER + "Impossible to open the file "
					+ fileName + " for reading.");
		}
	}

}
