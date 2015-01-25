package fr.ocelet.runtime.relation.aggregops;

import java.util.Iterator;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;

/**
 * Returns The minimum value taken from the argument vector. This AggregOperator
 * should only be used with number properties (real and int in Ocelet).
 * 
 * @see AggregOperator
 * @author Pascal Degenne, initial contribution
 */
public class Min<T extends Comparable<T>> implements AggregOperator<T,List<T>> {

	/**
	 * Computes all the candidate values and produces one unique value of the
	 * same type to be used for property affectation. In this case the minimum
	 * value is returned.
	 * 
	 * @param future
	 *            Vector of candidate values.
	 * @return The minimum value found in the argument future Vector.
	 */

	public T compute(List<T> future, T preval) {

		T choice = null;
		if (future.size() > 1) {
			for (Iterator<T> it = future.iterator(); it.hasNext();) {
				T val = it.next();
				if ((choice == null) || choice.compareTo(val) > 0)
					choice = val;
			}
		} else
			choice = future.get(0);
		return choice;
	}
}
