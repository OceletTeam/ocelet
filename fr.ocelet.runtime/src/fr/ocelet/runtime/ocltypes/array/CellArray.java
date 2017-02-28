package fr.ocelet.runtime.ocltypes.array;

import java.util.Collections;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.geom.ocltypes.SpatialType;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.GridManager;
import java.util.Iterator;
public class CellArray<T> extends ArrayInterface<T>{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid grid;
	private T ae;
	private int width;
	private int height;
	private Cell cell;
	
	public CellArray(T ae){
		this.ae = ae;
		this.cell = (Cell)((AbstractEntity)ae).getSpatialType();
		this.grid = GridManager.getInstance().get(cell.getNumGrid());
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
	public T get(int index){
		int xPos = Math.round(index / width);
		
		int yPos = Math.round(index % width);
		
		if(xPos > 0){
			this.cell.set(xPos, 0);
		}else{
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
	public boolean addAll(List<T> list) {
		return super.addAll((List<? extends T>)list);
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
	
	public Grid getGrid(){
		return grid;
	}
@Override
	public Iterator<T> iterator(){
		return new CellIterator();
	}
	
	public class CellIterator implements Iterator<T>{

		@Override
		public boolean hasNext() {
			if(cell.getX() == width - 1 && cell.getY() == height - 1){
				cell.set(0, 0);
			return false;
		}
		return true;
		}

		@Override
		public T next() {
			
			if(cell.getX() == width - 1){
				cell.setX(0);
				cell.setY(cell.getY() + 1);
			}else{
				cell.setX(cell.getX() + 1);
			}
		return ae;			
		}	
	}
}
