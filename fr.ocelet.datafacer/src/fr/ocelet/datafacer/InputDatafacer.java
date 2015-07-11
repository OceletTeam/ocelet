package fr.ocelet.datafacer;

import java.util.Iterator;

/**
 * The generic InputDatafacer definition. Defines the Input behavior and reading methods for most common simple types.
 * 
 * @author Pascal Degenne, Initial contribution
 */
public interface InputDatafacer extends Datafacer, Iterable<InputDataRecord> {

	public Iterator<InputDataRecord> iterator();

	InputDataRecord getLastRead();
	
	/**
	 * Release  any resource used by this datafacer
	 */
	abstract void close();
	
	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a String value
	 */
	public String readString(int colNumber);
	
	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as an Integer value
	 */
	public Integer readInteger(int colNumber);

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Long integer value
	 */
	public Long readLong(int colNumber);

	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Doule value
	 */
	public Double readDouble(int colNumber);
	
	/**
	 * Obtains the value from one column of the last read record using the
	 * column number.
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Boolean value
	 */
	public Boolean readBoolean(int colNumber);
	
	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a String value
	 */
	public String readString(String colName);
	
	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as an Integer value
	 */
	public Integer readInteger(String colName);
	
	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a Long integer value
	 */
	public Long readLong(String colName);
	
	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a Doule value
	 */
	public Double readDouble(String colName);
	
	/**
	 * Obtains the value from one column of the last read record using the
	 * column name.
	 * 
	 * @param colName
	 *            Name of the column to read (case sensitive)
	 * @return The value of the column as a Boolean value
	 */
	public Boolean readBoolean(String colName);
	
	/**
	 * Used to rewind the record iterator when calling readAll() several times.
	 */
	public void resetIterator();
}
