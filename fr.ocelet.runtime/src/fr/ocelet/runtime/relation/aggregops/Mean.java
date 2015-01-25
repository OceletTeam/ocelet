package fr.ocelet.runtime.relation.aggregops;

import java.util.Iterator;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;
import fr.ocelet.runtime.util.NumberConversion;

/**
 * Returns the mean of all the values contained in the argument vector. This
 * AggregOperator should only be used with number properties.
 * 
 * @see AggregOperator
 * @author Pascal Degenne, initial contribution
 */
public class Mean<T extends Number> implements AggregOperator<T, List<T>> {

	/**
	 * Computes all the candidate values and produces one unique value of the
	 * same type to be used for property affectation. In this case the mean
	 * value is returned.
	 * 
	 * @param future
	 *            Vector of candidate values.
	 * @return The mean of all the values contained in the argument vector.
	 */

	@SuppressWarnings("unchecked")
	public T compute(List<T> future, T preval) {

		double sum = 0.0;
		double result = future.get(0).doubleValue();
		if (future.size() > 1) {
			for (Iterator<T> it = (Iterator<T>) future.iterator(); it.hasNext();) {
				Number val = it.next();
				sum += val.doubleValue();
			}
			result = sum / future.size();
		}
		if (preval instanceof Integer)
			return (T) NumberConversion.convert2Int(result);
		if (preval instanceof Float)
			return (T) NumberConversion.convert2Float(result);
		if (preval instanceof Long)
			return (T) NumberConversion.convert2Long(result);
		return (T) new Double(result);
	}
}
