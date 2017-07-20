package fr.ocelet.runtime.ocltypes.array;

import java.util.ArrayList;
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
	@Override
	public boolean addAll(List<T> list) {
		return super.addAll((List<? extends T>)list);
		//return super.addAll((Collection<? extends T>)list);
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
	
	/*@Override
	public T get(int index){
		return super.get(index);
	}*/
	
	
	
	
	
}
