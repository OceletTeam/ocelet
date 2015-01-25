package fr.ocelet.runtime.relation;

/**
 * The most generic definition of how an InteractionGraph behaves.
 * 
 * @author Pascal Degenne, Initial contribution
 * 
 */
public interface InteractionGraph<E extends OcltEdge> extends Iterable<E> {

	public void beginTransaction();
	public void abortTransaction();
	public void endTransaction();

	/**
	 * Removes an edge form this graph
	 * 
	 * @param edge
	 */
	void disconnect(E edge);

	/**
	 * Removes a series of egdes
	 * 
	 * @param edges
	 *            The edges to be removed from this graph
	 */
	void disconnect(Iterable<E> edges);

	/**
	 * @return The number of edges in this interaction graph
	 */
	public int size();
	
}
