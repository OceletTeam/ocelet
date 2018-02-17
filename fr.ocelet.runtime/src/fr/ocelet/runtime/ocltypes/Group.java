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
package fr.ocelet.runtime.ocltypes;

import java.util.HashSet;

/**
 * A group is a set type for Ocelet.
 * 
 * @author Pascal Degenne - Initial contribution
 *
 * @param <T>
 */
public class Group<T> extends HashSet<T> {

	/**
	 * Adds several occurences of one same initializing value to this Group
	 * 
	 * @param numberOfObjects
	 *            Number of occurences to add.
	 * @param value
	 *            The initialization value that will be added to this Group
	 */
	public void addFill(int numberOfObjects, T value) {
		for (int i = 0; i < numberOfObjects; i++)
			add(value);
	}

	
	/**
	 * Static initializer
	 * 
	 * @param u A series of Objects
	 * @return A new Initialized List
	 */
	public static <U> Group<U> of(U... u) {
		Group<U> gr = new Group<U>();
		if (u != null)
			for (U p : u) {
				gr.add(p);
			}
		return gr;
	}
}
