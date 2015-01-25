package fr.ocelet.datafacer;

/**
 * Describes the behavior of all tabular datafacers that can be used for output.
 * @author Pascal Degenne, Initial contribution
 *
 */
public interface OutputDatafacer {
   
	/**
	 * Creates a new empty datarecord.
	 * @return An OutputDataRecord with no attribute initialization
	 */
	public OutputDataRecord createOutputDataRec();
	
}
