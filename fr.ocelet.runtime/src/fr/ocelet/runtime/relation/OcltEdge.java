package fr.ocelet.runtime.relation;

@SuppressWarnings("rawtypes")
public abstract class OcltEdge implements OcltGraphElement {

	protected static Long lastedgeid = 0L;
	protected final Long graph_Element_Identifier;
	protected InteractionGraph igraph;

	public OcltEdge() {
		this.graph_Element_Identifier = lastedgeid++;
	}

	public OcltEdge(InteractionGraph igr) {
		this.graph_Element_Identifier = lastedgeid++;
		this.igraph = igr;
	}

	public Object getGraph_Element_Identifier() {
		return graph_Element_Identifier;
	}

	public abstract OcltRole getRole(int i);
	
	@SuppressWarnings("unchecked")
	public void disconnect() {
		igraph.disconnect(this);
	}
}
