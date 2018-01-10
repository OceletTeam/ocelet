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
package fr.ocelet.runtime.ocltypes.array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import fr.ocelet.runtime.ocltypes.List;

public class ArrayInterface<T> extends ArrayList<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayInterface(){
		super();
	}
	/**
	 * Adds several occurences of one same initializing value to this List
	 * 
	 * @param numberOfObjects
	 *            Number of occurences to add.
	 * @param value
	 *            The initialization value that will be added to this List
	 */
	
	public  void addFill(int numberOfObjects, T value){
	this.addFill(numberOfObjects, value);	
	}

	/**
	 * Appends the specified element to the end of the list, but only if that
	 * element is not already present.
	 * 
	 * @param obj
	 */
	public boolean addU(T obj){
		return this.addU(obj);
	}
	
	/**
	 * Appends all of the elements in the specified List to the end of this list
	 * 
	 * @return true if the list could be changed as expected
	 */
	/*public boolean addAll(List<T> list){
		return this.addAll(list);
	}*/
	
	public boolean addAll(Collection<? extends T> list){
		
		return super.addAll(list);
		//return true;//Collections.addAll((Collection<? extends T>)this, ((List<T>)list).get(0));
		//return super.addAll(list);
	}
	/**
	 * @return The number of elements in the List equal to the specified object
	 * @param obj
	 */
	public int frequency(T obj){
		return this.frequency(obj);
	}

	/**
	 * Reverses the order of the elements in this List.
	 */
	public void reverse(){
		this.reverse();
	}

	/**
	 * Randomly permutes the elements of this List using a default source of
	 * randomness. All permutations occur with approximately equal likelihood.
	 */
	public void shuffle(){
		Collections.shuffle(this);
	}

	/**
	 * Rotates the elements in this list by the specified distance. After
	 * calling this method, the element at index i will be the element
	 * previously at index (i - distance) mod list.size(), for all values of i
	 * between 0 and list.size()-1, inclusive. (This method has no effect on the
	 * size of the list.)
	 * 
	 * @param distance
	 */
	public void rotate(int distance){
		Collections.rotate(this,distance);
	}

	/**
	 * Swaps the elements at the specified positions in this List. (If the
	 * specified positions are equal, invoking this method leaves the list
	 * unchanged.)
	 * 
	 * @param i
	 *            the index of one element to be swapped.
	 * @param j
	 *            the index of the other element to be swapped.
	 */
	public void swap(int i, int j){
		Collections.swap(this,i, j);
	}
	
	
	public void removeRange(int fromIndex, int toIndex){
		
	}
	
/*public ArrayInterface<T> sort(){
	Collections.sort(this);
	}*/
	/*public T get(int index){
		return this.get(index);
	}*/
	
}
