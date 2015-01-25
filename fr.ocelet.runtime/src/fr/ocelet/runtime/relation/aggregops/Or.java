package fr.ocelet.runtime.relation.aggregops;

import java.util.Iterator;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;

/**
 * Returns the computation of a logical OR on all the values contained in the
 * argument vector. This AggregOperator can only be used with boolean
 * properties.
 * 
 * @see AggregOperator
 * @author Pascal Degenne, initial contribution
 */
public class Or implements AggregOperator<Boolean, List<Boolean>> {

	/**
	 * Computes all the candidate values and produces one unique value of the
	 * same type to be used for property affectation. In this case the logical
	 * OR of all values is returned.
	 * 
	 * @param future
	 *            Vector of candidate values.
	 * @return The logical OR of all the values contained in the argument
	 *         vector.
	 */
	public Boolean compute(List<Boolean> future, Boolean preval) {

		Boolean result = future.get(0);
		if (future.size() > 1) {
			for (Iterator<Boolean> it = (Iterator<Boolean>) future.iterator(); it
					.hasNext();) {
				boolean val = it.next();
				result = result | val;
			}
		}
		return result;
	}
}
