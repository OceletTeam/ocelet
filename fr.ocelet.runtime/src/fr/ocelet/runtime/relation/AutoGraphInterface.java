package fr.ocelet.runtime.relation;


/**
 * Generic behavior for Interaction Di-Graphs (graphs with two distinctly typed roles).
 * 
 * @author Pascal Degenne, Initial contribution
 * 
 */
public interface AutoGraphInterface<E extends OcltEdge, Ro extends OcltRole>
		extends InteractionGraph<E> {

	/**
	 * Adds a new edge to this graph
	 * 
	 * @param left
	 *            One extremity of the new arc
	 * @param right
	 *            The other extremity of the new arc
	 * 
	 * @return The edge that has been created
	 */
	public E connect(Ro left, Ro right);
	
	public E createEdge(Ro gro, Ro lro);
	
	public AutoGraphInterface<E, Ro> getComplete();
}
