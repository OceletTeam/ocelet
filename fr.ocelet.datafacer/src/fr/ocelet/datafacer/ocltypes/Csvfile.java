package fr.ocelet.datafacer.ocltypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import fr.ocelet.datafacer.InputDataRecord;
import fr.ocelet.datafacer.InputDatafacer;
import fr.ocelet.runtime.TextFileWriter;
import fr.ocelet.runtime.entity.Entity;
import fr.ocelet.runtime.model.AbstractModel;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Datafacer specialized for reading Comma Separated Values format files
 * 
 * @author Pascal Degenne, Initial contribution
 */
public class Csvfile implements InputDatafacer, Iterator<InputDataRecord> {

	private final String ERR_HEADER = "Datafacer Csvfile: ";
	private final String SNO_DATA = "no_data";
	private final String DEFAULT_SEPARATOR = ";";
	protected BufferedReader reader;
	protected String currentLine;
	protected String separator;
	protected CsvRecord lastRead;
	protected HashMap<String, Integer> colIndex;
	protected String filename;

	/**
	 * Empty constructor, nothing special
	 */
	public Csvfile() {
		initDefaults();
	}

	/**
	 * Constructor initializing the filename. The default separator is ';'
	 * 
	 * @param fileName
	 *            Path and name of a comma separated value file.
	 */
	public Csvfile(String fileName) {
		initDefaults();
		setFileName(AbstractModel.getBasedir() + File.separator + fileName);
	}

	/**
	 * Constructor initializing the filename and the separator.
	 * 
	 * @param fileName
	 *            Path and name of a comma separated value file.
	 * @param sep
	 *            A separator String
	 */
	public Csvfile(String fileName, String sep) {
		initDefaults();
		setSeparator(sep);
		setFileName(AbstractModel.getBasedir() + File.separator + fileName);
	}

	protected void getHeader() {
		if (reader != null) {
			String header = null;
			try {
				String[] colnames;
				header = reader.readLine();
				colnames = header.split(separator);
				colIndex = new HashMap<String, Integer>();
				for (int i = 0; i < colnames.length; i++)
					colIndex.put(colnames[i], i);
			} catch (IOException e) {
				System.err
						.println(ERR_HEADER
								+ "An error occured while attempting to read the header line (column names) from "
								+ filename + ".");
			}
		}
	}

	protected void initDefaults() {
		setSeparator(DEFAULT_SEPARATOR);
	}

	/**
	 * Initialize and prepares the .csv file using the file name given in
	 * argument. If the file already exists, it is considered we are going to
	 * read it, we read the header. If the file does not already exist, we
	 * create it and write the header. Only the header of the file is read at
	 * this point, but it's availability is being checked. An error message is
	 * printed in case of initialization problem.
	 * 
	 * @param fileName
	 *            Name of the .csv file to read
	 */
	public void setFileName(String fileName) {
		this.filename = fileName;

		if (reader != null)
			try {
				reader.close();
			} catch (IOException e1) {
				// Could not close the reader ... er, so what ?
			}
		try {
			reader = new BufferedReader(new FileReader(this.filename));
			getHeader();
		} catch (FileNotFoundException e) {
			TextFileWriter.printToFile(this.filename, headerString());
		}
	}

	/**
	 * This method returns an empty string but we will call a specialized
	 * generated version
	 * 
	 * @return The header of the Csvfile, in write mode
	 */
	protected String headerString() {
		return "";
	}

	/**
	 * This method returns an empty string but we will call a specialized
	 * generated version
	 * 
	 * @return On line of coma separated values for the Csvfile
	 */
	protected String propsString(final Entity _entity) {
		return "";
	}

	/**
	 * Uses the entity given in argument to create a record and adds that record
	 * to the Csvfile. The values written to the file are those defined in the
	 * match statement of the datafacer definition.
	 * 
	 * @param ety
	 *            The entity to add
	 */
	public void append(Entity ety) {
		if (!TextFileWriter.isKnownFile(this.filename))
			TextFileWriter.printToFile(this.filename, headerString());
		TextFileWriter.printToFile(this.filename, propsString(ety));
	}

	/**
	 * Writes a series of coma seperated values into the Csvfile for every
	 * Entity found in the list given in argument. The values written to the
	 * file are those defined in the match statement of the datafacer
	 * definition.
	 * 
	 * @param lety
	 *            A list of entities
	 */
	public void append(List<? extends Entity> lety) {
		for (Entity ety : lety)
			append(ety);
	}

	/**
	 * Used to rewind the record iterator when calling readAll() several times.
	 */
	public void resetIterator() {
		setFileName(this.filename);
	}

	/**
	 * Changes the separator String
	 * 
	 * @param sep
	 */
	public void setSeparator(String sep) {
		this.separator = sep;
	}

	@Override
	public Iterator<InputDataRecord> iterator() {
		return this;
	}

	/**
	 * Checks if there is any more record that has not yet been read
	 * 
	 * @return true is there are more records to be read
	 */
	@Override
	public boolean hasNext() {
		boolean result = true;
		try {
			currentLine = reader.readLine();
		} catch (IOException e) {
			System.out.println(ERR_HEADER
					+ "An error occured while reading the file.");
			currentLine = null;
		}
		if (currentLine == null)
			result = false;
		return result;
	}

	/**
	 * Returns the next record read from the file.
	 * 
	 * @return A line String
	 */
	public CsvRecord next() {
		if (currentLine == null)
			hasNext();
		String result = currentLine;
		currentLine = null;
		lastRead = new CsvRecord(result, separator);
		return lastRead;
	}

	@Override
	public void remove() {
	}

	@Override
	public InputDataRecord getLastRead() {
		return this.lastRead;
	}

	@Override
	public String getErrHeader() {
		return this.ERR_HEADER;
	}

	public int getColumnIndexFromName(String colName) {
		Integer cix = colIndex.get(colName);
		if (cix == null)
			return -1;
		else
			return cix.intValue();
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a String value
	 */
	public String readString(int colNumber) {
		return getLastRead().readString(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as an Integer value
	 */
	public Integer readInteger(int colNumber) {
		return getLastRead().readInteger(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Long integer value
	 */
	public Long readLong(int colNumber) {
		return getLastRead().readLong(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Doule value
	 */
	public Double readDouble(int colNumber) {
		return getLastRead().readDouble(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Boolean value
	 */
	public Boolean readBoolean(int colNumber) {
		return getLastRead().readBoolean(colNumber);
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a String value
	 */
	public String readString(String colName) {
		int ci = getColumnIndexFromName(colName);
		if (ci >= 0)
			return getLastRead().readString(ci);
		else
			return SNO_DATA;
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as an Integer value
	 */
	public Integer readInteger(String colName) {
		int ci = getColumnIndexFromName(colName);
		if (ci >= 0)
			return getLastRead().readInteger(ci);
		else
			return 0;
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a Long Integer value
	 */
	public Long readLong(String colName) {
		int ci = getColumnIndexFromName(colName);
		if (ci >= 0)
			return getLastRead().readLong(ci);
		else
			return 0L;
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a Doule value
	 */
	public Double readDouble(String colName) {
		int ci = getColumnIndexFromName(colName);
		if (ci >= 0)
			return getLastRead().readDouble(ci);
		else
			return 0.0;
	}

	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a Boolean value
	 */
	public Boolean readBoolean(String colName) {
		int ci = getColumnIndexFromName(colName);
		if (ci >= 0)
			return getLastRead().readBoolean(ci);
		else
			return false;
	}

	/**
	 * Release any resource used by this datafacer
	 */
	@Override
	public void close() {
		if (reader != null)
			try {
				reader.close();
			} catch (IOException e) {
				System.err
						.println(ERR_HEADER
								+ "An error occured while attempting to close the file "
								+ filename + ".");

			}
	}

}
