package fr.ocelet.runtime.ocltypes;

import java.util.HashSet;

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
