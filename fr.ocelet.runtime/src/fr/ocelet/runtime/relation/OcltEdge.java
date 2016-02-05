/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
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

package fr.ocelet.runtime.relation;

/**
 * Generic edge of the interaction graphs
 * @author Pascal Degenne - Initial contribution
 *
 */
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
