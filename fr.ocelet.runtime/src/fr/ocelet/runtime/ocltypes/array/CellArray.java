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
package fr.ocelet.runtime.ocltypes.array;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.Grid;

/**
 * @author Mathieu Castets - Initial contribution
 *
 * @param <T>
 */
public class CellArray<T> extends ArrayInterface<T> {
	private static final long serialVersionUID = 1L;
	private Grid grid;
	private T ae;
	private int width;
	private int height;
	private Cell cell;
	private boolean start = true;

	public CellArray(T ae) {
		this.ae = ae;

		this.cell = (Cell) ((AbstractEntity) ae).getSpatialType();
		cell.setX(0);
		cell.setY(0);
		this.grid = cell.getGrid();
		this.width = grid.getWidth();
		this.height = grid.getHeight();
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

	}

	@Override
	public T get(int index) {
		int xPos = Math.round(index / width);

		int yPos = Math.round(index % width);

		if (xPos > 0) {
			this.cell.set(xPos, 0);
		} else {
			this.cell.set(xPos, yPos);
		}

		return ae;
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
	public boolean addAll(Collection<? extends T> list) {
		return false;
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
	 * Rotates the elements in this list by the specified distance. After calling
	 * this method, the element at index i will be the element previously at index
	 * (i - distance) mod list.size(), for all values of i between 0 and
	 * list.size()-1, inclusive. (This method has no effect on the size of the
	 * list.)
	 * 
	 * @param distance
	 */
	@Override
	public void rotate(int distance) {
		Collections.rotate(this, distance);
	}

	@Override
	public int size() {
		return grid.getWidth() * grid.getHeight();
	}

	/**
	 * Swaps the elements at the specified positions in this List. (If the specified
	 * positions are equal, invoking this method leaves the list unchanged.)
	 * 
	 * @param i
	 *            the index of one element to be swapped.
	 * @param j
	 *            the index of the other element to be swapped.
	 */
	@Override
	public void swap(int i, int j) {
	}

	/**
	 * Static initializer
	 * 
	 * @param u
	 *            A series of Objects
	 * @return A new Initialized List
	 */
	public static <U> List<U> of(U... u) {
		return null;
	}

	public Grid getGrid() {
		return grid;
	}

	@Override
	public Iterator<T> iterator() {
		return new CellIterator();
	}

	/*
	 * public abstract class Nexter<T extends AbstractEntity>{
	 * 
	 * public abstract T next();
	 * 
	 * 
	 * } public class Changer<T extends AbstractEntity>{
	 * 
	 * 
	 * public Nexter<T> nexter; public T next(){ return nexter.next(); } } public
	 * class FisrtNexter<T extends AbstractEntity> extends Nexter{
	 * 
	 * @Override public T next(){ cell.setX(0); cell.setY(0); return ae; } }
	 * 
	 * public class AfeterNexter extends Nexter<T>{
	 * 
	 * @Override public T next(){ if(cell.getX() == width - 1){ cell.setX(0);
	 * cell.setY(cell.getY() + 1); }else{ cell.setX(cell.getX() + 1); } return ae; }
	 * }
	 */
	public class CellIterator implements Iterator<T> {

		@Override
		public boolean hasNext() {
			if (cell.getX() == width - 1 && cell.getY() == height - 1) {
				cell.set(0, 0);
				start = true;
				return false;
			}
			return true;
		}

		@Override
		public T next() {
			if (start) {
				cell.set(0, 0);
				start = false;
			} else {
				if (cell.getX() == width - 1) {
					cell.setX(0);
					cell.setY(cell.getY() + 1);
				} else {
					cell.setX(cell.getX() + 1);
				}
			}
			return ae;
		}
	}
}
