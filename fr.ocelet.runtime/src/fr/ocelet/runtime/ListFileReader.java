package fr.ocelet.runtime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import fr.ocelet.runtime.ocltypes.List;

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

	/**
	 * Main for test purposes
	 */
//	public static void main(String[] args) {

//	}

}
