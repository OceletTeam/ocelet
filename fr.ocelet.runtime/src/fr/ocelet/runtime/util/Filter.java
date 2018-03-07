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

package fr.ocelet.runtime.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic filtering design pattern for Iterables.
 * 
 * @author Erik Rasmussen
 *         (http://erikras.com/2008/01/18/the-filter-pattern-java-conditional-abstraction-with-iterables/)
 *
 * @param <T>
 */
public abstract class Filter<T> {
	public abstract boolean passes(T object);

	public Iterator<T> filter(Iterator<T> iterator) {
		return new FilterIterator(iterator);
	}

	public Iterable<T> filter(final Iterable<T> iterable) {
		return new Iterable<T>() {
			public Iterator<T> iterator() {
				return filter(iterable.iterator());
			}
		};
	}

	private class FilterIterator implements Iterator<T> {
		private Iterator<T> iterator;
		private T next;

		private FilterIterator(Iterator<T> iterator) {
			this.iterator = iterator;
			toNext();
		}

		public boolean hasNext() {
			return next != null;
		}

		public T next() {
			if (next == null)
				throw new NoSuchElementException();
			T returnValue = next;
			toNext();
			return returnValue;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		private void toNext() {
			next = null;
			while (iterator.hasNext()) {
				T item = iterator.next();
				if (item != null && passes(item)) {
					next = item;
					break;
				}
			}
		}
	}
}