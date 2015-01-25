package fr.ocelet.runtime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A class to maintain a list of files where it is possible to write
 * lines of text, one by one, through a call to printline().
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
		return printToFile(filename,textline);
	}
	
	/**
	 * Prints one line of text at the end of the specified file.
	 * @param filename Name (and path) of the file to be updated. If the file does not exist, it will be created. 
	 * @param textLine Line of text to be added at the end of the file.
	 * @return Error message string if any problem occurred, or null if everything went fine.
	 */
	public static String printToFile(String filename, String textLine) {
		if(fmap==null){
			fmap = new HashMap<String, PrintWriter>();
		}
				 
		PrintWriter pw = fmap.get(filename);
		String errorString = null;
		if (pw == null) {
			try {
				File ff = new File(filename);
				if (ff.isAbsolute()) pw = new PrintWriter(filename);
				else if ((filename.indexOf(File.separator) < 0) || (filename.indexOf('/') < 0))
				pw = new PrintWriter(System.getProperty("user.dir") + File.separatorChar + "output" + File.separatorChar + filename);
				else pw = new PrintWriter(System.getProperty("user.dir") + File.separatorChar + filename);
				fmap.put(filename, pw);
			} catch (FileNotFoundException e) {
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
	 * Closes every file that could have been opened during the successive calls
	 * to printline.
	 */
	public void close() {
		for (Iterator<PrintWriter> pwit = fmap.values().iterator(); pwit
				.hasNext();) {
			pwit.next().close();
		}
	}
}
