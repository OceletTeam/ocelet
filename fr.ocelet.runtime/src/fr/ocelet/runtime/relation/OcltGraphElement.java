package fr.ocelet.runtime.relation;

/**
 * An interaction graph is made of Roles and Edges.
 * This interface represent what Roles and Edges have in common.
 * 
 * @author Pascal Degenne, Initial contribution.
 */
public interface OcltGraphElement {
   
	/**
     * Unique identifier provided by the graph implementation classes.
	 * @return An object identifying this graph element
	 */
	public Object getGraph_Element_Identifier();
}
