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

package fr.ocelet.datafacer;

import java.util.Iterator;

/**
 * The generic InputDatafacer definition.
 * Defines the Input behavior and reading methods for most common simple types.
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
