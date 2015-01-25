package fr.ocelet.runtime.relation;


/**
 * Generic behavior for Interaction Di-Graphs (graphs with two distinctly typed roles).
 * 
 * @author Pascal Degenne, Initial contribution
 * 
 */
public interface DiGraphInterface<E extends OcltEdge, R1 extends OcltRole, R2 extends OcltRole>
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
	public E connect(R1 left, R2 right);
	
	public E createEdge(R1 gro, R2 lro);
	
	public DiGraphInterface<E, R1, R2> getComplete();
}
