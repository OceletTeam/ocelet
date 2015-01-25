package fr.ocelet.datafacer;

public interface InputDataRecord {

	public Integer readInteger(int colNumber);
	public String readString(int colNumber);
	public Double readDouble(int colNumber);
	public Boolean readBoolean(int colNumber);
	public Long readLong(int colNumber);
	
    String getErrHeader();
    int getColumnIndexFromName(String colName);
    
   	/**
   	 * Obtains the value from one column using the column name.
   	 * 
   	 * @param colName
   	 *            Index of the column to read (first is #0)
   	 * @return The value of the column as a String value
   	 */
       public String readString(String colName);
       
   	/**
   	 * Obtains the value from one column of the last read record using the column name.
   	 * 
   	 * @param colName
   	 *            Index of the column to read (first is #0)
   	 * @return The value of the column as an Integer value
   	 */
       public Integer readInteger(String colName);

   	/**
   	 * Obtains the value from one column of the last read record using the column name.
   	 * 
   	 * @param colName
   	 *            Index of the column to read (first is #0)
   	 * @return The value of the column as an Long Integer value
   	 */
       public Long readLong(String colName);
       
   	/**
   	 * Obtains the value from one column of the last read record using the column name.
   	 * 
   	 * @param colName
   	 *            Index of the column to read (first is #0)
   	 * @return The value of the column as a Double value
   	 */
       public Double readDouble(String colName);
       
   	/**
   	 * Obtains the value from one column of the last read record using the column name.
   	 * 
   	 * @param colName
   	 *            Index of the column to read (first is #0)
   	 * @return The value of the column as a Boolean value
   	 */
       public Boolean readBoolean(String colName);
       
}
