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
