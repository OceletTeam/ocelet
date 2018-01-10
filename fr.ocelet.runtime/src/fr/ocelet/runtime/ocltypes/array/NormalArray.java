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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import fr.ocelet.runtime.ocltypes.List;

public class NormalArray<T> extends ArrayInterface<T> {

	
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NormalArray(){
		
	}
	
	
	 /**
	 * Adds several occurences of one same initializing value to this List
	 * 
	 * @param numberOfObjects
	 *            Number of occurences to add.
	 * @param value
	 *            The initialization value that will be added to this List
	 */
	@Override
	public void addFill(int numberOfObjects, T value) {
		for (int i = 0; i < numberOfObjects; i++)
			add(value);
	}

	/**
	 * Appends the specified element to the end of the list, but only if that
	 * element is not already present.
	 * 
	 * @param obj
	 */
	@Override
	public boolean addU(T obj) {
		if (!contains(obj))
			return add(obj);
		else
			return false;
	}
	
	/**
	 * Appends all of the elements in the specified List to the end of this list
	 * 
	 * @return true if the list could be changed as expected
	 */
	/*@Override
	public boolean addAll(List<T> list) {
		System.out.println("ADD ALL");
		return addAll((List<? extends T>)list);
		//return super.addAll((Collection<? extends T>)list);
	}*/
	@Override
	public boolean addAll(Collection<? extends T> list) {
		//return Collections.addAll(list, list.toArray(list.get(0)));
		//Collections.addAll(this, Arrays.asList(list.toArrays()));
		//this.addAll(list)
		return super.addAll((Collection<? extends T>)list);
	}
	/**
	 * @return The number of elements in the List equal to the specified object
	 * @param obj
	 */
	@Override
	public int frequency(T obj) {
		return Collections.frequency(this, obj);
	}

	/**
	 * Reverses the order of the elements in this List.
	 */
	@Override
	public void reverse() {
		Collections.reverse(this);
	}

	/**
	 * Randomly permutes the elements of this List using a default source of
	 * randomness. All permutations occur with approximately equal likelihood.
	 */
	@Override
	public void shuffle() {
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
	@Override
	public void rotate(int distance) {
		Collections.rotate(this, distance);
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
	@Override
	public void swap(int i, int j) {
		Collections.swap(this, i, j);
	}

	/**
	 * Static initializer
	 * 
	 * @param u
	 *            A series of Objects
	 * @return A new Initialized List
	 */
	public static <U> List<U> of(U... u) {
		List<U> las = new List<U>();
		if (u != null)
			for (U p : u) {
				las.add(p);
			}
		return las;
	}
	
	@Override
	public void removeRange(int fromIndex, int toIndex){
		super.removeRange(fromIndex, toIndex);
	}
	
	@Override
	public Object[] toArray(){
		return super.toArray();
	}
	/*@Override
	public T get(int index){
		return super.get(index);
	}
	
	@Override
	public boolean add(T value){
		System.out.println("NORMAL ADD");
		return super.add(value);
	}
	
	@Override
	public T set(int index, T element){
		return super.set(index,  element);
	}*/
	
	
	
}
