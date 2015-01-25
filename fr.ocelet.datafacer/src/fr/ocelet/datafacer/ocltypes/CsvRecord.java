package fr.ocelet.datafacer.ocltypes;

import fr.ocelet.datafacer.InputDataRecord;

public class CsvRecord implements InputDataRecord {

	protected boolean colNamesAvailable;

	@Override
	public int getColumnIndexFromName(String colName) {
		if (colNamesAvailable) {
			// TODO
			return 0;
		} else {
			System.out
					.println(getErrHeader()
							+ " Impossible to use the attribute named "
							+ colName + ".");
			System.out
					.println("  Check that the column name is correct and that a header was provided for the csv file.");
			return -1;
		}
	}

	private final String ERR_HEADER = "Datafacer Csvfile: ";
	protected String[] cols;

	/**
	 * Creates a new CsvRecord by parsing the line given in argument
	 * 
	 * @param newline
	 *            A String with each field separated by a separator
	 * @param separator
	 *            The separator String
	 */
	public CsvRecord(String newline, String separator) {
		this.cols = newline.split(separator);
	}

	/**
	 * Obtains the value from one column of the record
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a String value
	 */
	@Override
	public String readString(int colNumber) {
		String result = "?";
		try {
			result = cols[colNumber];
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			System.out.println(ERR_HEADER
					+ "Impossible to reach column number " + colNumber
					+ ". The number of available columns is " + cols.length
					+ ". For your information, the first column is number 0.");
		} catch (NumberFormatException nfe) {
			System.out.println(ERR_HEADER + "Impossible to convert the column "
					+ colNumber
					+ " into an Integer value. Content found is that column :"
					+ cols[colNumber]);
		}
		return result;
	}

	/**
	 * Obtains the value from one column of the record
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as an Integer value
	 */
	@Override
	public Integer readInteger(int colNumber) {
		Integer result = new Integer(0);
		try {
			result = new Integer(cols[colNumber]);
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			System.out.println(ERR_HEADER
					+ "Impossible to reach column number " + colNumber
					+ ". The number of available columns is " + cols.length
					+ ". For your information, the first column is number 0.");
		} catch (NumberFormatException nfe) {
			System.out.println(ERR_HEADER + "Impossible to convert the column "
					+ colNumber
					+ " into an Integer value. Content found is that column :"
					+ cols[colNumber]);
		}
		return result;
	}
	

	/**
	 * Obtains the value from one column of the record
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Long Integer value
	 */
	@Override
	public Long readLong(int colNumber) {
		Long result = new Long(0);
		try {
			result = new Long(cols[colNumber]);
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			System.out.println(ERR_HEADER
					+ "Impossible to reach column number " + colNumber
					+ ". The number of available columns is " + cols.length
					+ ". For your information, the first column is number 0.");
		} catch (NumberFormatException nfe) {
			System.out.println(ERR_HEADER + "Impossible to convert the column "
					+ colNumber
					+ " into a Long Integer value. Content found is that column :"
					+ cols[colNumber]);
		}
		return result;
	}

	
	
	
	/**
	 * Obtains the value from one column of the record
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Doule value
	 */
	@Override
	public Double readDouble(int colNumber) {
		Double result = new Double(0.0);
		try {
			result = new Double(cols[colNumber]);
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			System.out.println(ERR_HEADER
					+ "Impossible to reach column number " + colNumber
					+ ". The number of available columns is " + cols.length
					+ ". For your information, the first column is number 0.");
		} catch (NumberFormatException nfe) {
			System.out.println(ERR_HEADER + "Impossible to convert the column "
					+ colNumber
					+ " into an Integer value. Content found is that column :"
					+ cols[colNumber]);
		}
		return result;
	}

	/**
	 * Obtains the value from one column of the record
	 * 
	 * @param colNumber
	 *            Index of the column to read (first is #0)
	 * @return The value of the column in boolean type
	 */
	@Override
	public Boolean readBoolean(int colNumber) {
		boolean result = false;
		try {
			result = new Boolean(cols[colNumber]).booleanValue();
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			System.out.println(ERR_HEADER
					+ "Impossible to reach column number " + colNumber
					+ ". The number of available columns is " + cols.length
					+ ". For your information, the first column is number 0.");
		}
		return result;
	}

	@Override
	public String getErrHeader() {
		return this.ERR_HEADER;
	}
	
   	/**
   	 * Obtains the value from one column using the column name.
   	 * 
   	 * @param colName
   	 *            Index of the column to read (first is #0)
   	 * @return The value of the column as a String value
	 * @deprecated Use CsvFile method instead
   	 */
       public String readString(String colName) {
       	int colindex = getColumnIndexFromName(colName);
       	if (colindex > 0) return readString(colindex);
       	else {
       		System.out.println(getErrHeader()
   					+ "Impossible to obtain a value from the attribute name :"+colName);
       		return new String("?");
       	}
       }
       
	/**
	 * Obtains the value from one column of the last read record using the column name.
	 * 
	 * @param colName
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as an Integer value
	 * @deprecated Use CsvFile method instead
	 */
    public Integer readInteger(String colName) {
    	int colindex = getColumnIndexFromName(colName);
    	if (colindex > 0) return readInteger(colindex);
    	else {
    		System.out.println(getErrHeader()
					+ "Impossible to obtain a value from the attribute name :"+colName);
    		return new Integer(0);
    	}
    }

	/**
	 * Obtains the value from one column of the last read record using the column name.
	 * 
	 * @param colName
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as an Long Integer value
	 * @deprecated Use CsvFile method instead
	 */
    public Long readLong(String colName) {
    	int colindex = getColumnIndexFromName(colName);
    	if (colindex > 0) return readLong(colindex);
    	else {
    		System.out.println(getErrHeader()
					+ "Impossible to obtain a value from the attribute name :"+colName);
    		return new Long(0);
    	}
    }

    
    
	/**
	 * Obtains the value from one column of the last read record using the column name.
	 * 
	 * @param colName
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Double value
	 * @deprecated Use CsvFile method instead
	 */
    public Double readDouble(String colName) {
    	int colindex = getColumnIndexFromName(colName);
    	if (colindex > 0) return readDouble(colindex);
    	else {
    		System.out.println(getErrHeader()
					+ "Impossible to obtain a value from the attribute name :"+colName);
    		return new Double(0.0);
    	}
    }
    
	/**
	 * Obtains the value from one column of the last read record using the column name.
	 * 
	 * @param colName
	 *            Index of the column to read (first is #0)
	 * @return The value of the column as a Boolean value
	 * @deprecated Use CsvFile method instead
	 */
    public Boolean readBoolean(String colName) {
    	int colindex = getColumnIndexFromName(colName);
    	if (colindex > 0) return readBoolean(colindex);
    	else {
    		System.out.println(getErrHeader()
					+ "Impossible to obtain a value from the attribute name :"+colName);
    		return new Boolean(false);
    	}
    }

}
