/*
 *  Ocelet spatial modelling language.   www.ocelet.org
 *  Copyright Cirad 2010-2017
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.geotools.coverageio.gdal.aig.AIGFormat;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.ocltypes.array.ArrayInterface;
import fr.ocelet.runtime.ocltypes.array.CellArray;
import fr.ocelet.runtime.ocltypes.array.NormalArray;

/**
 * 
 * @author Pascal Degenne  - Initial contribution
 * @author Mathieu Castets - Observer based version
 *
 */
@SuppressWarnings("serial")
public class List<T> extends ArrayList<T> {


	private ArrayInterface<T> observer;

	public List(){
		observer = new Observer();
	}
	
	public void addFill(int numberOfObjects, T value) {
		observer.addFill(numberOfObjects, value);

	}
	
	public boolean addU(T obj) {
		return observer.addU(obj);

	}
	@Override
	public boolean addAll(Collection<? extends T> list) {
		return observer.addAll(list);//super.addAll((Collection<? extends T>)list);
	}
	
	
	public int frequency(T obj) {
		return observer.frequency(obj);
		//return Collections.frequency(this, obj);
	}

	public void reverse() {
		observer.reverse();
		//Collections.reverse(this);
	}

	public void shuffle() {
		observer.shuffle();
		//Collections.shuffle(this);
	}

	public void rotate(int distance) {
		observer.rotate(distance);
		//Collections.rotate(this, distance);
	}

	public void swap(int i, int j) {
		observer.swap(i,  j);
		//Collections.swap(this, i, j);
	}

	public static <U> List<U> of(U... u) {

		List<U> las = new List<U>();
		if (u != null)
			for (U p : u) {
				las.add(p);
			}
		return las;
	}

	public class Observer extends ArrayInterface<T>{
		
		private ArrayInterface<T> ai;
		
		public Observer(){
			ai = new ArrayInterface<T>();
		}
		
		@Override
		public boolean add(T value){
			instanciate(value);
			
			return observer.add(value);
		}
		
		public void instanciate(T value){
			if(value instanceof AbstractEntity){
				AbstractEntity ae = (AbstractEntity)value;
				if(ae.getSpatialType() instanceof Cell){
					ai = new CellArray<T>(value);
				}
			} else {
					ai = new NormalArray<T>();
				}
			observer = ai;
		}
		
		public void instanciateGetter(){
			if(ai.size() > 0){
				
			}
		}

		public void addFill(int numberOfObjects, T value) {
			instanciate(value);
			observer.addFill(numberOfObjects, value);	
		}

		public boolean addU(T obj) {
			instanciate(obj);
			return observer.addU(obj);
		}
		@Override
		public boolean addAll(Collection<? extends T> list) {
			if(list.size() > 0)
				instanciate(((List<T>)list).get(0));
			 return ai.addAll(list);
		}


		public int frequency(T obj) {
			return ai.frequency(obj);
		}


		public void reverse() {
			ai.reverse();
		}


		public void shuffle() {
			ai.shuffle();
		}


		public void rotate(int distance) {
			ai.rotate(distance);
		}


		public void swap(int i, int j) {
			ai.swap(i,  j);
		}
		
		


		public <U> List<U> of(U... u) {
			List<U> las = new List<U>();
			if (u != null)
				for (U p : u) {
					las.add(p);
				}
			return las;
		}
		@Override
		public T get(int index) {			
			return ai.get(index);
		}
		
		


	}

	@Override
	public Iterator<T> iterator(){
		return observer.iterator();
	}
	
	@Override
	public T get(int index) {
		return observer.get(index);
	}
	@Override
	public boolean add(T value){
		return observer.add(value);
	}
	
	@Override
	public int size(){
		return observer.size();
	}
	
	@Override
	public void add(int index, T element){
		observer.add(index, element);
	}
	
	@Override
	public void clear(){
		observer.clear();
	}
	
	@Override
	public boolean contains(Object o){
		return observer.contains(o);
	}
	
	@Override
	public int indexOf(Object o){
		return observer.indexOf(o);
	}
	
	@Override
	public boolean isEmpty(){
		return observer.isEmpty();
	}
	
	@Override
	public int lastIndexOf(Object o){
		return observer.lastIndexOf(o);
	}
	
	@Override
	public T remove(int index){
		return observer.remove(index);
	}

	@Override
	public boolean remove(Object o){
		return observer.remove(o);
	}
	
	@Override
	public T set(int index, T element){
		return observer.set(index, element);
	}
	@Override
	protected void removeRange(int fromIndex, int toIndex){
		observer.removeRange(fromIndex, toIndex);
	}
	@Override
	public List<T> subList(int fromIndex, int toIndex){
		java.util.List<T> newList = observer.subList(fromIndex, toIndex);
		List<T> l = new List<T>();
		for(T t : newList){
			l.add(t);
		}
		return l;
	}
	
	@Override
	public Object[] toArray(){
		return observer.toArray();
	}
	
	/*@Override
	public List<T> sort(){
		List<T> list = new List<T>();
		for(T t : observer){
			list.add(t);
		}
		Collections.sort(list);
		return (List<T>)observer.sort();
	}*/
	
	

}
