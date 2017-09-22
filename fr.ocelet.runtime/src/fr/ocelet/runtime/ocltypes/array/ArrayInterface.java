package fr.ocelet.runtime.ocltypes.array;

import java.util.ArrayList;
import java.util.Collections;

import fr.ocelet.runtime.ocltypes.List;

public class ArrayInterface<T> extends ArrayList<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


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
	public boolean addAll(List<T> list){
		return this.addAll(list);
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
