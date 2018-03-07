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

package fr.ocelet.runtime.entity;

import java.util.ArrayList;
import java.util.Iterator;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;
import fr.ocelet.runtime.relation.aggregops.Any;

/**
 * Entity's historic property. A property has the capacity to hold past values.
 * They also are coherent with the transaction mechanism present in entities,
 * meaning that several values can be given as candidates to be set to this
 * property in the future and an AggregOperator will be used to compute the next
 * state of the property at commit time.
 * 
 * @author Pascal Degenne - Initial contribution
 */
public class Hproperty<T> implements Iterable<T> {

	final int DEFAULT_MAX_ARCS = 1;

	ArrayList<T> content;
	List<T> future;
	boolean containsFuture;
	int index;
	int historySize;
	AggregOperator<T,List<T>> ao;
	boolean usepreval;

	
	public Hproperty(){
		historySize = 1;
		content = new ArrayList<T>(historySize);
		future = new List<T>();
		containsFuture = false;
		index = 0;
		ao = new Any<T>();
		usepreval = false;
	}
	
	/**
	 * Produces an instance of a Hproperty with the specified history size.
	 * 
	 * @param size
	 *            Number of history steps
	 */
	public Hproperty(int size) {
		historySize = size;
		content = new ArrayList<T>(historySize);
		future = new List<T>();
		containsFuture = false;
		index = 0;
		ao = new Any<T>();
		usepreval = false;
	}
	
	
	/**
	 * Produces an instance of a Hproperty with the specified history size
	 * 
	 * @param size
	 *            Number of history steps
	 * @param futsz
	 *            Initial number of expected concurrent values (to be processed
	 *            by AffectOperators).
	 */
	public Hproperty(int size, int futsz) {
		historySize = size;
		content = new ArrayList<T>(historySize);
		future = new List<T>();
		containsFuture = false;
		index = 0;
		ao = new Any<T>();
		usepreval = false;
	}

	/**
	 * Changes the actual state of this property with the argument val. The
	 * history is shifted by one step. Any future candidate value is removed
	 * from the candidate list.
	 * 
	 * @param val
	 *            The new value to affect to this Hproperty
	 */
	public void set(T val) {
		if (content.size() < historySize)
			content.add(val);
		else
			content.set(index, val);
		if (++index == historySize)
			index = 0;
		flushFuture();
	}

	/**
	 * Adds a candidate value to the candidate list. That list will be processed
	 * later using an AggregOperator.
	 * 
	 * @param futureval
	 *            The candidate value
	 */
	public void setFuture(T futureval) {
		containsFuture = true;
		future.add(futureval);
	}

	/**
	 * Set an AggregOperator to process the list of candidate values to be
	 * affected to this Hproperty. If no AggregOperator has been specified
	 * through this method, the Any operator will be used by default.
	 * 
	 * @param newao
	 *            An AggregOperator
	 */
	public void setAffectOperator(AggregOperator<T,List<T>> newao, boolean usepreval) {
		this.ao = newao;
		this.usepreval = usepreval;
	}

	/**
	 * Processes the list of candidate values using the AggregOperator assigned
	 * to this Hproperty. If no AggregOperator has been specified through this
	 * method, the Any operator is used by default.
	 */
	public void commitFuture() {
		if (containsFuture) {
			if (usepreval) this.future.add(get());
			set(ao.compute(this.future,this.get()));
		}
	}

	/**
	 * Clears the list of candidate values.
	 */
	public void flushFuture() {
		future.clear();
		containsFuture = false;
	}

	/**
	 * @return The actual value of this Hproperty
	 */
	public T get() {
		return get(0);
	}

	/**
	 * Obtain the historic value of this Hproperty pointed to by the argument
	 * pastIndex
	 * 
	 * @param pastIndex
	 *            Negative or null index to point the historic value to obtain.
	 *            If 0 the actual value is returned. If -1 the last step value
	 *            is returned, and so on.
	 * @return A historic value indexed by pastIndex
	 */
	public T get(int pastIndex) {
		int gx = index + pastIndex - 1;
		if (gx < 0)
			gx += content.size();
		if (gx < 0) return null; else return content.get(gx);
	}

	/**
	 * Overrides the Object.toString() method.
	 */
	public String toString() {
		String hps = "";
		if (content.size() > 1) {
			hps += "[";
			String comma = "";
			for (Iterator<T> cit = this.iterator(); cit.hasNext();) {
				hps += comma+cit.next();
				comma=",";
			}
			hps += "]";
		} else
			hps += content.get(0);
		return hps;
	}

	/**
	 * Returns an Iterator on the historic values of this Hproperty. The first
	 * value returned is the oldest, and the last is the most recent (actual
	 * state of this Hproperty).
	 */
	public Iterator<T> iterator() {
		return new HistoryIterator();
	}

	public class HistoryIterator implements Iterator<T> {

		private int next = 0;
		private int nbleft;

		public HistoryIterator() {
			next = index;
			nbleft = historySize;
		}

		public boolean hasNext() {
			return (nbleft > 0);
		}

		public T next() {

			T val = content.get(next++);
			if (next == historySize)
				next = 0;
			nbleft--;
			return val;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
